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

package org.openecomp.portalapp.portal.scheduler.policy;

/**
 * The Class RestObject.
 *
 * @param <T> the generic type
 */
public class RestObject<T> {

	/**
	 * Generic version of the RestObject class.
	 *
	 */
    // T stands for "Type"
    private T t;
    
    /** The status code. */
    private int statusCode= 0;
    
    /**
     * Sets the.
     *
     * @param t the t
     */
    public void set(T t) { this.t = t; }
    
    /**
     * Gets the.
     *
     * @return the t
     */
    public T get() { return t; }
	
    /**
     * Sets the status code.
     *
     * @param v the new status code
     */
    public void setStatusCode(int v) { this.statusCode = v; }
    
    /**
     * Gets the status code.
     *
     * @return the status code
     */
    public int getStatusCode() { return this.statusCode; }
    
}