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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExternalAccessPerms other = (ExternalAccessPerms) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
}
