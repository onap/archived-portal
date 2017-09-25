package org.openecomp.portalapp.portal.scheduler.restobjects;

public class GetTimeSlotsRestObject<T> extends RestObject<T> {

	public String uuid;
	
	public void setUUID(String uuid) { this.uuid = uuid; }
	    
	public String getUUID() { return this.uuid; }

}
