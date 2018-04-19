/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
package org.onap.portalapp.portal.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

public abstract class ParallelExecutor<T> {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ParallelExecutor.class);

	protected static abstract class ThreadOperation<T> {
		public abstract T execute(List<Object> parms);
	}

	protected abstract ThreadOperation<T> getThreadOperation();

	private static class CallableOperationThread<T> implements Callable<T> {

		List<Object> parms;

		private ThreadOperation<T> operation;

		public CallableOperationThread(ThreadOperation<T> operation, List<Object> parms) {
			this.parms = parms;
			this.operation = operation;
		}

		@Override
		public T call() throws Exception {
			return this.operation.execute(this.parms);
		}

	}

	public List<T> performAllOperations(int ThreadPoolSize, List<List<Object>> listOfParms) {
		List<T> result = new ArrayList<T>();
		if (ThreadPoolSize > 0 && listOfParms != null) {
			ExecutorService executor = Executors.newFixedThreadPool(ThreadPoolSize);
			List<Future<T>> list = new ArrayList<Future<T>>();
			for (List<Object> parms : listOfParms) {
				CallableOperationThread<T> getter = new CallableOperationThread<T>(this.getThreadOperation(), parms);
				Future<T> submit = executor.submit(getter);
				list.add(submit);
			}
			for (Future<T> future : list) {
				try {
					if (future != null) {
						result.add(future.get());
					}
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "performAllOperations failed", e);
				}
			}
			executor.shutdown();
		}
		return result;
	}

}
