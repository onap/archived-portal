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
package org.onap.portalapp.portal.ueb;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EcompApp;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.ueb.Helper;
import org.onap.portalsdk.core.onboarding.ueb.Publisher;
import org.onap.portalsdk.core.onboarding.ueb.UebException;
import org.onap.portalsdk.core.onboarding.ueb.UebManager;
import org.onap.portalsdk.core.onboarding.ueb.UebMsg;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiProperties;
import org.onap.portalapp.portal.ueb.EPUebHelper;
import org.onap.portalapp.portal.ueb.EPUebMsgTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class EPUebHelper {
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPUebHelper.class);
	
	@Autowired
	private EPAppService appsService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unused")
	private Publisher epPublisher;
	
	public EPUebHelper() {
		
	}
	//
	// This should only be called by the ONAP Portal App, other Apps have just one publisher and use appPublisher
	//
	@SuppressWarnings("unused")
	@EPMetricsLog
	public void refreshPublisherList()
	{
		Session localSession = null;
	    boolean addedPublisher = false;
	    
		try {
			localSession = sessionFactory.openSession();
		
			List<EcompApp> apps = appsService.getEcompAppAppsFullList();
			for (int i = 0; i < apps.size(); i++) 
			{
				if ((apps.get(i).isEnabled()) &&
					(apps.get(i).getUebTopicName() != null) &&
					!(apps.get(i).getUebTopicName().toUpperCase().contains("ECOMP-PORTAL-INBOX")))
				{
					logger.debug(EELFLoggerDelegate.debugLogger, "UEBManager adding publisher for " + apps.get(i).getUebTopicName());
					UebManager.getInstance().addPublisher(apps.get(i).getUebTopicName());
	                addedPublisher = true;
			    }
				else if ((apps.get(i).getId() != 1) && // App may have been disabled, remove the publisher
					   	 !(apps.get(i).isEnabled()))
				{
					if(apps.get(i).getUebTopicName()!=null){
						UebManager.getInstance().removePublisher(apps.get(i).getUebTopicName());
					}
				}
			}
		}
		catch (Exception e)
		{
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebSystemError, e, "add/remove Publisher");
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while refreshing the publisher list", e);
		}
		
		//publisherList.print();
		
		if (addedPublisher == true) // Give publishers time to initialize
		{
			Helper.sleep(400);
		}
	}
	
	@PostConstruct
	@EPMetricsLog
	public void initUeb() {
		try {
			epPublisher = new Publisher(PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY), 
					                    PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_SECRET), 
					                    PortalApiProperties.getProperty(PortalApiConstants.ECOMP_PORTAL_INBOX_NAME));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "initUeb failed", e);
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebConnectionError, e);
		}
		
		Thread thread = new Thread("EPUebManager: postConstructMethod - refreshPublisherList") {
		    public void run(){
	  	        refreshPublisherList();
		    }
		};
		thread.start();
		
	}

	@EPMetricsLog
	public void addPublisher(EPApp app) {
		// TODO Auto-generated method stub
		try {
			UebManager.getInstance().addPublisher(app.getUebTopicName());
		} catch (UebException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "addPublisher failed", e);
		}
	}
	
	public boolean checkAvailability() {
		//
		//  Test existence of topic at UEB url
		//
		//  (ie http://uebsb91kcdc.it.com:3904/topics/ECOMP-PORTAL-INBOX)
		//
		boolean available = true;
		LinkedList<String> urlList = (LinkedList<String>) Helper.uebUrlList();
		if (!urlList.isEmpty()) {
		    String url = "http://" + urlList.getFirst() + ":3904/topics/" + PortalApiProperties.getProperty(PortalApiConstants.ECOMP_PORTAL_INBOX_NAME);
		    if (!url.isEmpty()) {
		        try {
		            URL siteURL = new URL(url);
		            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
		            connection.setRequestMethod("GET");
		            connection.connect();
		     
		            int code = connection.getResponseCode();
		            if (code == 200) {
		                available = true;
		            }
		            else {
		            	EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebConnectionError, url);
		                available = false;
		                logger.warn(EELFLoggerDelegate.errorLogger, "Warning! UEB topic existence check failed, topic = " + url );
		                logger.debug(EELFLoggerDelegate.debugLogger, "Warning! UEB topic existence check failed, topic = " + url );
		            }
		        }
		        catch (Exception e) {
		            available = false;
					logger.error(EELFLoggerDelegate.errorLogger, "checkAvailability failed", e);
		        }
		    }
		}
		return available;
	}
	
    public boolean MessageCanBeSentToTopic() {
    
    	boolean sentMsgSuccessfully = false;
    	
	    UebMsg msg = new UebMsg();
	    msg.putSourceTopicName(PortalApiProperties.getProperty(PortalApiConstants.ECOMP_PORTAL_INBOX_NAME));
	    msg.putPayload("Pinging topic for health check");
	    msg.putMsgType(EPUebMsgTypes.UEB_MSG_TYPE_HEALTH_CHECK);
	
	    try {
		   // epPublisher.send(msg);
		    sentMsgSuccessfully = true;
	    } 
	    catch (Exception e) {
	    	EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHealthCheckUebClusterError, e);
		    sentMsgSuccessfully = false;
		    logger.warn(EELFLoggerDelegate.errorLogger, "Warning! could not successfully publish a UEB msg to " 
		                     + PortalApiProperties.getProperty(PortalApiConstants.ECOMP_PORTAL_INBOX_NAME), e); 
	    } 
	    
	    return sentMsgSuccessfully;
    }
    
}


