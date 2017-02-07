/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.logging.format;

import com.att.eelf.i18n.EELFResolvableErrorEnum;
import com.att.eelf.i18n.EELFResourceManager;

/**
 * 
 * @author rc580q
 * Add ECOMP Portal Specific Error Code Enums here, for generic
 * ones (ones you think are useful not only Portal but also SDK), add it
 * to the enum class AppMessagesEnum defined in SDK.
 */
public enum EPErrorCodesEnum implements EELFResolvableErrorEnum {
	BERESTAPIAUTHENTICATIONERROR,
	BEHTTPCONNECTIONERROR_ONE_ARGUMENT,
	BEUEBAUTHENTICATIONERROR_ONE_ARGUMENT,
	
	INTERNALAUTHENTICATIONINFO_ONE_ARGUMENT,
	INTERNALAUTHENTICATIONWARNING_ONE_ARGUMENT,
	INTERNALAUTHENTICATIONERROR_ONE_ARGUMENT,
	INTERNALAUTHENTICATIONFATAL_ONE_ARGUMENT,
	
	BEHEALTHCHECKRECOVERY,
	BEHEALTHCHECKMYSQLRECOVERY,
	BEHEALTHCHECKUEBCLUSTERRECOVERY,
	FEHEALTHCHECKRECOVERY,
	BeHEALTHCHECKERROR,
	
	BEHEALTHCHECKMYSQLERROR,
	BEHEALTHCHECKUEBCLUSTERERROR,
	FEHEALTHCHECKERROR,
	BEUEBCONNECTIONERROR_ONE_ARGUMENT,
	BEUEBUNKOWNHOSTERROR_ONE_ARGUMENT,
	BEUEBREGISTERONBOARDINGAPPERROR,
	
	INTERNALCONNECTIONINFO_ONE_ARGUMENT,
	INTERNALCONNECTIONWARNING_ONE_ARGUMENT,
	INTERNALCONNECTIONERROR_ONE_ARGUMENT,
	INTERNALCONNECTIONFATAL_ONE_ARGUMENT,
	
	BEUEBOBJECTNOTFOUNDERROR_ONE_ARGUMENT,
	BEUSERMISSINGERROR_ONE_ARGUMENT,
	
	BEUSERINACTIVEWARNING_ONE_ARGUMENT,
	BEUSERADMINPRIVILEGESINFO_ONE_ARGUMENT,
	
	BEINVALIDJSONINPUT,
	BEINCORRECTHTTPSTATUSERROR,
		
	BEINITIALIZATIONERROR,
	BEUEBSYSTEMERROR,
	BEDAOSYSTEMERROR,
	BESYSTEMERROR,
	BEEXECUTEROLLBACKERROR,
	
	FEHTTPLOGGINGERROR,
	FEPORTALSERVLETERROR,
	BEDAOCLOSESESSIONERROR,
	
	BERESTAPIGENERALERROR,
	FEHEALTHCHECKGENERALERROR,
	
	INTERNALUNEXPECTEDINFO_ONE_ARGUMENT,
	INTERNALUNEXPECTEDWARNING_ONE_ARGUMENT,
	INTERNALUNEXPECTEDERROR_ONE_ARGUMENT,
	INTERNALUNEXPECTEDFATAL_ONE_ARGUMENT,
	;
	
	/**
     * Static initializer to ensure the resource bundles for this class are loaded...
     * Here this application loads messages from three bundles
     */
    static {
        EELFResourceManager.loadMessageBundle("org/openecomp/portalapp/portal/logging/format/ApplicationCodes");
    }
}
