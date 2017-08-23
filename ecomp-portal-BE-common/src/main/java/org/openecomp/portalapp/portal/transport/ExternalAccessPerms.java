package org.openecomp.portalapp.portal.transport;

import java.io.Serializable;

public class ExternalAccessPerms implements Serializable, Comparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -200964838466882602L;
	public String type;
	public String instance;
	public String action;
	public String description;
	
	
	public ExternalAccessPerms() {
		super();
	}
	
	
	
	public ExternalAccessPerms(String type, String instance, String action, String description) {
		super();
		this.type = type;
		this.instance = instance;
		this.action = action;
		this.description = description;
	}

	public ExternalAccessPerms(String type, String instance, String action) {
		super();
		this.type = type;
		this.instance = instance;
		this.action = action;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}
	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public int compareTo(Object obj){
	ExternalAccessPerms other = (ExternalAccessPerms)obj;

	String c1 = getInstance();
	String c2 = other.getInstance();

	return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
	}
	
	
}
