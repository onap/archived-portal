
package org.openecomp.portalapp.portal.scheduler;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.openecomp.portalapp.portal.scheduler.restobjects.RestObject;


@Service
public interface SchedulerRestInterfaceIfc {

	public void initRestClient();

	public <T> void Get(T t, String sourceId, String path, org.openecomp.portalapp.portal.scheduler.restobjects.RestObject<T> restObject ) throws Exception;

	public <T> void Delete(T t, JSONObject requestDetails, String sourceID, String path, RestObject<T> restObject)
			throws Exception;

	public <T> void Post(T t, JSONObject r, String path, RestObject<T> restObject) throws Exception;

	public void logRequest(JSONObject requestDetails);
}