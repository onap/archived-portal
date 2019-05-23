/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.onap.portalapp.portal.domain.MicroserviceData;
import org.onap.portalapp.portal.domain.MicroserviceParameter;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service("microserviceService")
@EnableAspectJAutoProxy
@EPMetricsLog
public class MicroserviceServiceImpl implements MicroserviceService {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MicroserviceServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	public Long saveMicroservice(MicroserviceData newService) throws Exception {
		if (newService.getPassword() != null)
			newService.setPassword(encryptedPassword(newService.getPassword()));
		getDataAccessService().saveDomainObject(newService, null);
		return newService.getId();
	}

	public void saveServiceParameters(long serviceId, List<MicroserviceParameter> list) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			MicroserviceParameter para = list.get(i);
			para.setServiceId(serviceId);
			getDataAccessService().saveDomainObject(para, null);
		}
	}

	@Override
	public MicroserviceData getMicroserviceDataById(long id) {
		MicroserviceData data = null;
		try {
			List<Criterion> restrictionsList = new ArrayList<Criterion>();
			Criterion idCriterion = Restrictions.eq("id", id);
			restrictionsList.add(idCriterion);
			data = (MicroserviceData) dataAccessService.getList(MicroserviceData.class, null, restrictionsList, null).get(0);
			
			data.setParameterList(getServiceParameters(id));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMicroserviceDataById failed", e);
			throw e;
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MicroserviceData> getMicroserviceData() throws Exception {
		List<MicroserviceData> list = (List<MicroserviceData>) dataAccessService.getList(MicroserviceData.class, null);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPassword() != null)
				list.get(i).setPassword(EPCommonSystemProperties.APP_DISPLAY_PASSWORD);  //to hide password from get request
			list.get(i).setParameterList(getServiceParameters(list.get(i).getId()));
		}
		return list;
	}

	private List<MicroserviceParameter> getServiceParameters(long serviceId) {
		List<MicroserviceParameter> list = getMicroServiceParametersList(serviceId);
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<MicroserviceParameter> getMicroServiceParametersList(long serviceId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion serviceIdCriterion = Restrictions.eq("serviceId", serviceId);
		restrictionsList.add(serviceIdCriterion);
		return (List<MicroserviceParameter>) dataAccessService.getList(MicroserviceParameter.class, null, restrictionsList, null);
	}

	@Override
	public void deleteMicroservice(long serviceId) throws Exception {

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("serviceId", Long.toString(serviceId));

			dataAccessService.executeNamedQuery("deleteMicroserviceParameter", params, null);
			dataAccessService.executeNamedQuery("deleteMicroservice", params, null);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(EELFLoggerDelegate.errorLogger, "deleteMicroservice failed", e);
			throw e;
		}
	}

	@Override
	public void updateMicroservice(long serviceId, MicroserviceData newService) throws Exception {
		try {
			newService.setId(serviceId);
			if (newService.getPassword() != null){
				if(newService.getPassword().equals(EPCommonSystemProperties.APP_DISPLAY_PASSWORD)){
					MicroserviceData oldMS = getMicroserviceDataById(serviceId);
					newService.setPassword(oldMS.getPassword()); // keep the old password
				}else
					newService.setPassword(encryptedPassword(newService.getPassword())); //new password
			}
			getDataAccessService().saveDomainObject(newService, null);
			List<MicroserviceParameter> oldService = getServiceParameters(serviceId);
			boolean foundParam;
			for (int i = 0; i < oldService.size(); i++) {
				foundParam = false;
				for (int n = 0; n < newService.getParameterList().size(); n++) {
					if (newService.getParameterList().get(n).getId().equals(oldService.get(i).getId())) {
						foundParam = true;
						break;
					}
				}
				if (foundParam == false) {
					MicroserviceParameter pd = oldService.get(i);
					getDataAccessService().deleteDomainObject(pd, null);
				}
			}
			for (int i = 0; i < newService.getParameterList().size(); i++) {
				MicroserviceParameter param = newService.getParameterList().get(i);
				param.setServiceId(serviceId);
				getDataAccessService().saveDomainObject(param, null);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "updateMicroservice failed", e);
			throw e;
		}
		saveServiceParameters(serviceId, newService.getParameterList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MicroserviceParameter> getParametersById(long serviceId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("serviceId", serviceId);
		restrictionsList.add(contextIdCrit);
		List<MicroserviceParameter> list = (List<MicroserviceParameter>) dataAccessService
				.getList(MicroserviceParameter.class, null, restrictionsList, null);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"getParametersById: microservice parameters list size: " + list.size());
		return list;
	}

	private String decryptedPassword(String encryptedPwd) throws Exception {
		String result = "";
		if (encryptedPwd != null && !encryptedPwd.isEmpty()) {
			try {
				result = CipherUtil.decryptPKC(encryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword failed", e);
				throw e;
			}
		}
		return result;
	}

	private String encryptedPassword(String decryptedPwd) throws Exception {
		String result = "";
		if (decryptedPwd != null && !decryptedPwd.isEmpty()) {
			try {
				result = CipherUtil.encryptPKC(decryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "encryptedPassword failed", e);
				throw e;
			}
		}
		return result;
	}

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

}
