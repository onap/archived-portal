/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.openecomp.portalapp.portal.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;

/**
 * 
 * @author Vladimir Turovets This class is used to create HashMap<String, T>
 *         from the list of T objects. We suppose that
 *         1) T object contains field 'parmName' or getter 'getParmName()'.
 *         The value of object.parmName or object.getParmName() is used as
 *         a key in created hashMap.
 *         2) for all objects in the list 'parmName' or getter
 *         'getParmName().toString' has to be unique and not null.
 *         This class has one function only:
 *         HashMap<String, T> hashMap(List<T> list, String name) and returns
 *         hash map created from list.
 *
 * @param <T>
 * Type 
 */
public class HashMapFromList<T> {
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HashMapFromList.class);
	
	public HashMap<String, T> hashMap(List<T> list, String name) {
		HashMap<String, T> result = new HashMap<String, T>();
		if (list == null || list.size() == 0 || name == null) {
			return result;
		}
		name = name.trim();
		if (name.length() > 0) {
			T object = list.get(0);
			try {
				String parmName = name;
				Field field = object.getClass().getField(parmName);
				for (T obj : list) {
					try {
						Object o = field.get(obj);
						if (o != null)
							result.put(o.toString(), obj);
					} catch (Exception e1) {
						logger.error(EELFLoggerDelegate.errorLogger, "hashMap failed 1: object of class " + object.getClass().getName() + ", field " + parmName, e1);
						return new HashMap<String, T>();
					}
				}
			} catch (Exception e) {
				String getterName = "get" + (name.length() == 1 ? name.toUpperCase() : (name.substring(0, 1).toUpperCase() + name.substring(1)));
				try {
					Class<?>[] parmClasses = null;
					Method method = object.getClass().getMethod(getterName, parmClasses);
					Object[] parmValues = new Object[0];
					for (T obj : list) {
						try {
							Object o = method.invoke(obj, parmValues);
							if (o != null)
								result.put(o.toString(), obj);
						} catch (Exception e2) {
							logger.error(EELFLoggerDelegate.errorLogger, "hashMap failed 2: object of class " + object.getClass().getName() + ", method " + getterName, e2);
							return new HashMap<String, T>();
						}
					}
				} catch (Exception e3) {
					logger.error(EELFLoggerDelegate.errorLogger, "hashMap failed 3: object of class " + object.getClass().getName() + ", bad field '" + name + "' or method '" + getterName + "()", e3);
					return new HashMap<String, T>();
				}
			}
		}
		if (list.size() != result.size()) {
			logger.warn(EELFLoggerDelegate.errorLogger, "Duplicated or empty keys were found!!!");
		}
		return result;
	}

}
