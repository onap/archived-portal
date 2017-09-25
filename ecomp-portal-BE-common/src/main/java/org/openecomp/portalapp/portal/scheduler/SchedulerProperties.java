package org.openecomp.portalapp.portal.scheduler;

import org.openecomp.portalsdk.core.util.SystemProperties;


public class SchedulerProperties extends SystemProperties { 
	
	public static final String SCHEDULER_USER_NAME_VAL =  "scheduler.user.name";;
		
	public static final String SCHEDULER_PASSWORD_VAL = "scheduler.password";
	
	public static final String SCHEDULER_SERVER_URL_VAL = "scheduler.server.url";
	
	public static final String SCHEDULER_CREATE_NEW_VNF_CHANGE_INSTANCE_VAL = "scheduler.create.new.vnf.change.instance";
	
	public static final String SCHEDULER_GET_TIME_SLOTS = "scheduler.get.time.slots";
	
	public static final String SCHEDULER_SUBMIT_NEW_VNF_CHANGE = "scheduler.submit.new.vnf.change";

}
