package org.openecomp.portalapp.portal.transport;

public class ExternalAccessUserRoleDetail {
		
	private String name;
	private ExternalRoleDescription description;
	
	
	/**
	 * 
	 */
	public ExternalAccessUserRoleDetail() {
		super();
	}

	public ExternalAccessUserRoleDetail(String name, ExternalRoleDescription description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ExternalRoleDescription getDescription() {
		return description;
	}
	public void setDescription(ExternalRoleDescription description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ExternalAccessUserRoleDetail other = (ExternalAccessUserRoleDetail) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
