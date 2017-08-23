package org.openecomp.portalapp.portal.domain;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("rawtypes")
public class CentralRoleFunction extends DomainVo implements java.io.Serializable, Comparable{
	
	   /**
	 * 
	 */
	private static final long serialVersionUID = -4018975640065252688L;
	   private String code;
	   private String name;
	   @JsonIgnore
	   private Long appId;
	   @JsonIgnore
	   private Long roleId;
	   private String editUrl;
	   
	   
	public CentralRoleFunction() {
	
	}
	
	
	public CentralRoleFunction(Long id, String code, String name, Long appId, String editUrl) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.appId = appId;
		this.editUrl = editUrl;
	}

	public CentralRoleFunction(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the appId
	 */
	public Long getAppId() {
		return appId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	/**
	 * @return the editUrl
	 */
	public String getEditUrl() {
		return editUrl;
	}
	/**
	 * @param editUrl the editUrl to set
	 */
	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}
    public int compareTo(Object obj){
        String c1 = getName();
        String c2 = ((CentralRoleFunction)obj).getName();

        return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
      }
	
	
   
}
