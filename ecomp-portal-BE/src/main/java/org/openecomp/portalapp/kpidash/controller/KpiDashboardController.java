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
package org.openecomp.portalapp.kpidash.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.kpidash.model.KpiApiStats;
import org.openecomp.portalapp.kpidash.model.KpiServiceSupported;
import org.openecomp.portalapp.kpidash.model.KpiUserStoryStats;
import org.openecomp.portalapp.kpidash.service.KpiDashboardService;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
//public class KpiDashboardController extends EPUnRestrictedBaseController {
public class KpiDashboardController extends EPRestrictedBaseController {
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(KpiDashboardController.class);
	
	@Autowired
	KpiDashboardService service;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/get_user_stories_stats" }, method = RequestMethod.GET, produces = "application/json")	
	public List<KpiUserStoryStats> getUserStories(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<KpiUserStoryStats> userStories = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				userStories = (List<KpiUserStoryStats>) service.getUserStories();
				EcompPortalUtils.logAndSerializeObject("/portalApi/get_user_stories_stats", "GET result =", userStories);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getUserApps operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return userStories;
	}

	@RequestMapping(value = { "/portalApi/save_user_stories_stats" }, method = RequestMethod.GET, produces = "application/json")
	public void saveUserStories(@RequestBody KpiUserStoryStats kpiUserStoryStats) {
		service.saveUserStories(kpiUserStoryStats);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/update_user_stories_stats" }, method = RequestMethod.POST)
	public List<KpiUserStoryStats> updateUserStories(@RequestBody KpiUserStoryStats kpiUserStoryStats) {
		service.updateUserStories(kpiUserStoryStats);
		return (List<KpiUserStoryStats>) service.getUserStories();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/get_user_api_stats" }, method = RequestMethod.GET, produces = "application/json")	
	public List<KpiApiStats> getUserApis(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<KpiApiStats> apiStats = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				apiStats = (List<KpiApiStats>) service.getUserApis();
				EcompPortalUtils.logAndSerializeObject("/portalApi/get_user_api_stats", "GET result =", apiStats);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getUserApps operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return apiStats;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/get_kpi_service_supported" }, method = RequestMethod.GET, produces = "application/json")	
	public List<KpiServiceSupported> getKpiServiceSupported(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<KpiServiceSupported> kpiServiceSupported = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				kpiServiceSupported = (List<KpiServiceSupported>) service.getKpiServiceSupported();
				EcompPortalUtils.logAndSerializeObject("/portalApi/get_kpi_service_supported", "GET result =", kpiServiceSupported);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getUserApps operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return kpiServiceSupported;
//		return (List<KpiServiceSupported>) service.getKpiServiceSupported();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/update_user_api_stats" }, method = RequestMethod.POST)
	public List<KpiApiStats> updateUserApis(@RequestBody KpiApiStats kpiApiStats) {
		service.updateUserApis(kpiApiStats);
		return (List<KpiApiStats>) service.getUserApis();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/execute_get_published_delivered" }, method = RequestMethod.GET, produces = "application/json")
	public List<String> getPublishedDelivered(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<String> publishedDelivered = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				publishedDelivered = service.executeGetBytesPublishedDelivered();
				EcompPortalUtils.logAndSerializeObject("/portalApi/execute_get_published_delivered", "GET result =", publishedDelivered);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getLOCStatsCat operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return publishedDelivered;
	//	return service.executeGetBytesPublishedDelivered();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/execute_get_feed_stats" }, method = RequestMethod.GET, produces = "application/json")
	public List<Long> getFeedStats(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<Long> feedStats= null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				feedStats = service.executeGetFeedStats();
				EcompPortalUtils.logAndSerializeObject("/portalApi/execute_get_feed_stats", "GET result =", feedStats);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getLOCStatsCat operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return feedStats;
//		return service.executeGetFeedStats();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/get_loc_stats_cat" }, method = RequestMethod.GET, produces = "application/json")	
	public List<String> getLOCStatsCat(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<String> LOCStatsCat = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				LOCStatsCat = service.getLOCStatsCat();
				EcompPortalUtils.logAndSerializeObject("/portalApi/get_loc_stats_cat", "GET result =", LOCStatsCat);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getLOCStatsCat operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return LOCStatsCat;		
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/portalApi/get_loc_stats" }, method = RequestMethod.GET, produces = "application/json")	
	public List<Long> getLOCStats(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<Long> LOCStats = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				LOCStats = service.getLOCStats();
				EcompPortalUtils.logAndSerializeObject("/portalApi/get_loc_stats", "GET result =", LOCStats);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getLOCStats operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		return LOCStats;
	}

	@RequestMapping(value = { "/portalApi/get_geo_map_url" }, method = RequestMethod.GET, produces = "text/plain")
	public String getGeoMapUrl(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		String geoMapUrl = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				geoMapUrl = service.getGeoMapUrl();
				EcompPortalUtils.logAndSerializeObject("/portalApi/get_geo_map_url", "GET result =", geoMapUrl);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getGeoMapUrl operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		
		return geoMapUrl;
	}

	@RequestMapping(value = { "/portalApi/get_rcloud_a_url" }, method = RequestMethod.GET, produces = "text/plain")
	public String getRCloudAUrl(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		String rcloudUrl = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				rcloudUrl = service.getRCloudAUrl();
				EcompPortalUtils.logAndSerializeObject("/portalApi/get_rcloud_a_url", "GET result =", rcloudUrl);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getGeoMapUrl operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		
		return rcloudUrl;
	}

	@RequestMapping(value = { "/portalApi/get_geo_map_api_url" }, method = RequestMethod.GET, produces = "text/plain")
	public String getGeoMapApiUrl(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		String geoMapApiUrl = null;
		
		try {
			if (user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "getUserApps");
			} else {
				geoMapApiUrl = service.getGeoMapApiUrl();
				EcompPortalUtils.logAndSerializeObject("/portalApi/get_geo_map_api_url", "GET result =", geoMapApiUrl);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while performing getGeoMapApiUrl operation, Details: " + EcompPortalUtils.getStackTrace(e));
		}
		
		return geoMapApiUrl;
	}
}
