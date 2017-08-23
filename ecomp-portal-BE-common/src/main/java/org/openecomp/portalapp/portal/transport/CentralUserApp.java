package org.openecomp.portalapp.portal.transport;

@SuppressWarnings("rawtypes")
public class CentralUserApp implements Comparable{
	
	private Long userId;
	private CentralApp app;
	private CentralRole role;
	private Short priority;
	
	
	
	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public CentralApp getApp() {
		return app;
	}



	public void setApp(CentralApp app) {
		this.app = app;
	}



	public CentralRole getRole() {
		return role;
	}



	public void setRole(CentralRole role) {
		this.role = role;
	}



	public Short getPriority() {
		return priority;
	}



	public void setPriority(Short priority) {
		this.priority = priority;
	}



	public int compareTo(Object other){
	    CentralUserApp castOther = (CentralUserApp) other;

	    Long c1 = (this.getUserId()==null ? 0 : this.getUserId()) + (this.priority==null ? 0 : this.priority);
	    Long c2 = (castOther.getUserId()==null ? 0 : castOther.getUserId()) + (castOther.getApp()==null||castOther.getApp().getId()==null ? 0 : castOther.getApp().getId()) + (castOther.priority==null ? 0 : castOther.priority);

	    return c1.compareTo(c2);
	}
	
}
