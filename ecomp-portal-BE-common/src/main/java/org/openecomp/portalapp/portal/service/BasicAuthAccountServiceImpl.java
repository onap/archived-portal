package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.openecomp.portalapp.portal.domain.BasicAuthCredentials;
import org.openecomp.portalapp.portal.domain.EPEndpoint;
import org.openecomp.portalapp.portal.domain.EPEndpointAccount;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service("basicAuthAccountService")
@EnableAspectJAutoProxy
@EPMetricsLog
public class BasicAuthAccountServiceImpl implements BasicAuthAccountService{
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MicroserviceServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	@Override
	public Long saveBasicAuthAccount(BasicAuthCredentials newCredential) throws Exception {
		if (newCredential.getPassword() != null)
			newCredential.setPassword(encryptedPassword(newCredential.getPassword()));
		try{
			getDataAccessService().saveDomainObject(newCredential, null);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "saveBasicAuthAccount() failed", e);
			throw e;
		}
		return newCredential.getId();
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public Long saveEndpoints(EPEndpoint endpoint) throws Exception {
		
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion NameCrit = Restrictions.eq("name", endpoint.getName());
		restrictionsList.add(NameCrit);

		List<EPEndpoint> tempList = (List<EPEndpoint>) dataAccessService.getList(EPEndpoint.class, null,
				restrictionsList, null);
		if (tempList.size() != 0) {
			return tempList.get(0).getId();
		} else {
			getDataAccessService().saveDomainObject(endpoint, null);
			return endpoint.getId();
		}
		
	}
	
	@Override
	public void saveEndpointAccount(Long accountId, Long endpointId) throws Exception {
		EPEndpointAccount record = new EPEndpointAccount();
		record.setAccount_id(accountId);
		record.setEp_id(endpointId);
		try {
			getDataAccessService().saveDomainObject(record, null);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveEndpointAccount() failed", e);
			throw e;
		}

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void updateBasicAuthAccount(Long accountId, BasicAuthCredentials newCredential) throws Exception {
		try {
			newCredential.setId(accountId);
			if (newCredential.getPassword() != null)
				newCredential.setPassword(encryptedPassword(newCredential.getPassword()));
			getDataAccessService().saveDomainObject(newCredential, null);
			
			List<EPEndpoint> endpoints = newCredential.getEndpoints();
			List<EPEndpoint> orig_points = getEPEndpoints(accountId);
			
			for(EPEndpoint temp_ep: orig_points){
				boolean flag = false;
				for(EPEndpoint temp_ep2: endpoints){
					if(temp_ep2.getId() == temp_ep.getId())
						flag = true;
				}
				if(!flag){
					Map<String, String> params = new HashMap<String, String>();
					params.put("accountId", Long.toString(accountId));
					params.put("epId", Long.toString(temp_ep.getId()));
					dataAccessService.executeNamedQuery("deleteAccountEndpointRecord", params, null);
				}
			}
			
			
			for(int i = 0; i < endpoints.size(); i++){
				
				List<Criterion> restrictionsList = new ArrayList<Criterion>();
				Criterion IdCrit = Restrictions.eq("id", endpoints.get(i).getId());
				restrictionsList.add(IdCrit);
				Criterion NameCrit = Restrictions.eq("name", endpoints.get(i).getName());
				restrictionsList.add(NameCrit);
				List<EPEndpoint> tempList = (List<EPEndpoint>) dataAccessService
						.getList(EPEndpoint.class, null, restrictionsList, null);
				if(tempList.size() == 0){
					if(endpoints.get(i).getId() != null){
						//delete the record endpoints.get(i).getId(), accountId						
						Map<String, String> params = new HashMap<String, String>();
						params.put("accountId", Long.toString(accountId));
						params.put("epId", Long.toString(endpoints.get(i).getId()));
						dataAccessService.executeNamedQuery("deleteAccountEndpointRecord", params, null);
						endpoints.get(i).setId(null);
					}
					//create a new endpoint
					Long ep_id = saveEndpoints(endpoints.get(i));
					saveEndpointAccount(accountId, ep_id);						 
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "updateBasicAuthAccount() failed", e);
			throw e;
		}
	}

	@Override
	public List<BasicAuthCredentials> getAccountData() throws Exception {
		@SuppressWarnings("unchecked")
		List<BasicAuthCredentials> list = (List<BasicAuthCredentials>) dataAccessService.getList(BasicAuthCredentials.class, null);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPassword() != null)
				list.get(i).setPassword(decryptedPassword(list.get(i).getPassword()));
			list.get(i).setEndpoints(getEPEndpoints(list.get(i).getId()));
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private List<EPEndpoint> getEPEndpoints(long accountId) {
		List<EPEndpoint> result = new ArrayList<>();
		List<EPEndpointAccount> list = (List<EPEndpointAccount>) dataAccessService
				.getList(EPEndpointAccount.class, " where account_id = '" + accountId + "'", null, null);
		for(int i = 0; i < list.size(); i++){
			result.add((EPEndpoint) dataAccessService.getDomainObject(EPEndpoint.class, list.get(i).getEp_id(), null));
		}
		return result;
	}
	
	@Override
	public void deleteEndpointAccout(Long accountId) throws Exception {
		try{
			Map<String, String> params = new HashMap<String, String>();
			params.put("accountId", Long.toString(accountId));
			
			dataAccessService.executeNamedQuery("deleteAccountEndpoint", params, null);
			dataAccessService.executeNamedQuery("deleteBasicAuthAccount", params, null);
			
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "deleteEndpointAccout() failed", e);
			throw e;
		}
	}
	
	private String decryptedPassword(String encryptedPwd) throws Exception {
		String result = "";
		if (encryptedPwd != null & encryptedPwd.length() > 0) {
			try {
				result = CipherUtil.decrypt(encryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword() failed", e);
				throw e;
			}
		}
		return result;
	}

	private String encryptedPassword(String decryptedPwd) throws Exception {
		String result = "";
		if (decryptedPwd != null & decryptedPwd.length() > 0) {
			try {
				result = CipherUtil.encrypt(decryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "encryptedPassword() failed", e);
				throw e;
			}
		}
		return result;
	}
	
	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}
}
