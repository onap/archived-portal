
package org.openecomp.portalapp.portal.scheduler.policy;

public class PolicyRestInterfaceFactory {

	
	public static PolicyRestInterfaceIfc getInstance () {
		PolicyRestInterfaceIfc obj = null;

		obj = new PolicyRestInterface();
		
		return ( obj );
	}
}