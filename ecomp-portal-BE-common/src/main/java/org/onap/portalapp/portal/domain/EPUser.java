/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
package org.onap.portalapp.portal.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EPUser extends User {
	    
	    private Long   orgId;
	    private Long   managerId;
	    private String firstName;
	    private String middleInitial;
	    private String lastName;
	    private String phone;
	    private String fax;
	    private String cellular;
	    private String email;
	    private Long   addressId;
	    private String alertMethodCd;
	    private String hrid;
	    private String orgUserId;
	    private String orgCode;
	    private String address1;
	    private String address2;
	    private String city;
	    private String state;
	    private String zipCode;
	    private String country;
	    private String orgManagerUserId;
	    private String locationClli;
	    private String businessCountryCode;
	    private String businessCountryName;
	    private String businessUnit;
	    private String businessUnitName;
	    private String department;
	    private String departmentName;
	    private String companyCode;
	    private String company;
	    private String zipCodeSuffix;
	    private String jobTitle;
	    private String commandChain;
	    private String siloStatus;
	    private String costCenter;
	    private String financialLocCode;
	    
	    
	  
	    private String loginId;
	    private String loginPwd;
	    private Date   lastLoginDate;
	    private boolean active;
	    private boolean internal;
	    private Long    selectedProfileId;
	    private Long timeZoneId;
	    private boolean online;
	    private String chatId;
	
	    private static final long serialVersionUID = 1L;

	    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPUser.class);
		private static final String ECOMP_PORTAL_NAME = "ECOMP";
		private boolean isGuest = false;
		
		private SortedSet<EPUserApp> userApps = new TreeSet<EPUserApp>();
		private SortedSet<EPRole> pseudoRoles = new TreeSet<EPRole>();

	    public EPUser() {}

	    public Long getAddressId() {
	        return addressId;
	    }

	    public String getAlertMethodCd() {
	        return alertMethodCd;
	    }

	    public String getCellular() {
	        return cellular;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public String getFax() {
	        return fax;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public String getHrid() {
	        return hrid;
	    }

	    public Date getLastLoginDate() {
	        return lastLoginDate;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    @JsonIgnore
	    public String getFullName() {
	        return getFirstName() + " " + getLastName();	
	    }
	    
	    public String getLoginId() {
	        return loginId;
	    }

	    public String getLoginPwd() {
	        return loginPwd;
	    }

	    public Long getManagerId() {
	        return managerId;
	    }

	    public String getMiddleInitial() {
	        return middleInitial;
	    }

	    public String getOrgCode() {
	        return orgCode;
	    }

	    public Long getOrgId() {
	        return orgId;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public String getOrgUserId() {
	        return orgUserId;
	    }

	    public boolean getActive() {
	        return active;
	    }

	    public boolean getInternal() {
	        return internal;
	    }

	    public String getAddress1() {
	        return address1;
	    }

	    public String getAddress2() {
	        return address2;
	    }

	    public String getCity() {
	        return city;
	    }

	    public String getCountry() {
	        return country;
	    }

	    public String getState() {
	        return state;
	    }

	    public String getZipCode() {
	        return zipCode;
	    }

	    public String getBusinessCountryCode() {
	        return businessCountryCode;
	    }

	    public String getCommandChain() {
	        return commandChain;
	    }

	    public String getCompany() {
	        return company;
	    }

	    public String getCompanyCode() {
	        return companyCode;
	    }

	    public String getDepartment() {
	        return department;
	    }

	    public String getJobTitle() {
	        return jobTitle;
	    }

	    public String getLocationClli() {
	        return locationClli;
	    }

	    public String getOrgManagerUserId() {
	        return orgManagerUserId;
	    }

	    public String getZipCodeSuffix() {
	        return zipCodeSuffix;
	    }

	    public String getBusinessCountryName() {
	        return businessCountryName;
	    }

	    public Long getSelectedProfileId() {
	        return selectedProfileId;
	    }

	    public void setAddressId(Long addressId) {
	        this.addressId = addressId;
	    }

	    public void setAlertMethodCd(String alertMethodCd) {
	        this.alertMethodCd = alertMethodCd;
	    }

	    public void setCellular(String cellular) {
	        this.cellular = cellular;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public void setFax(String fax) {
	        this.fax = fax;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public void setHrid(String hrid) {
	        this.hrid = hrid;
	    }

	    public void setLastLoginDate(Date lastLoginDate) {
	        this.lastLoginDate = lastLoginDate;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public void setLoginId(String loginId) {
	        this.loginId = loginId;
	    }

	    public void setLoginPwd(String loginPwd) {
	        this.loginPwd = loginPwd;
	    }

	    public void setManagerId(Long managerId) {
	        this.managerId = managerId;
	    }

	    public void setMiddleInitial(String middleInitial) {
	        this.middleInitial = middleInitial;
	    }

	    public void setOrgCode(String orgCode) {
	        this.orgCode = orgCode;
	    }

	    public void setOrgId(Long orgId) {
	        this.orgId = orgId;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public void setOrgUserId(String orgUserId) {
	        this.orgUserId = orgUserId;
	    }

	    public void setActive(boolean active) {
	        this.active = active;
	    }

	    public void setInternal(boolean internal) {
	        this.internal = internal;
	    }

	    public void setAddress1(String address1) {
	        this.address1 = address1;
	    }

	    public void setAddress2(String address2) {
	        this.address2 = address2;
	    }

	    public void setCity(String city) {
	        this.city = city;
	    }

	    public void setCountry(String country) {
	        this.country = country;
	    }

	    public void setState(String state) {
	        this.state = state;
	    }

	    public void setZipCode(String zipCode) {
	        this.zipCode = zipCode;
	    }

	    public void setBusinessCountryCode(String businessCountryCode) {
	        this.businessCountryCode = businessCountryCode;
	    }

	    public void setCommandChain(String commandChain) {
	        this.commandChain = commandChain;
	    }

	    public void setCompany(String company) {
	        this.company = company;
	    }

	    public void setCompanyCode(String companyCode) {
	        this.companyCode = companyCode;
	    }

	    public void setDepartment(String department) {
	        this.department = department;
	    }

	    public void setJobTitle(String jobTitle) {
	        this.jobTitle = jobTitle;
	    }

	    public void setLocationClli(String locationClli) {
	        this.locationClli = locationClli;
	    }

	    public void setOrgManagerUserId(String orgManagerUserId) {
	        this.orgManagerUserId = orgManagerUserId;
	    }

	    public void setZipCodeSuffix(String zipCodeSuffix) {
	        this.zipCodeSuffix = zipCodeSuffix;
	    }

	    public void setBusinessCountryName(String businessCountryName) {
	        this.businessCountryName = businessCountryName;
	    }

	    public SortedSet<EPRole> getPseudoRoles() {
			return pseudoRoles;
		}
	    
	    public void setPseudoRoles(SortedSet<EPRole> pseudoRoles) {
	        this.pseudoRoles = pseudoRoles;
	    }

	    public void setSelectedProfileId(Long selectedProfileId) {
	        this.selectedProfileId = selectedProfileId;
	    }
	    
		public Long getTimeZoneId() {
			return timeZoneId;
		}

		public void setTimeZoneId(Long timeZoneId) {
			this.timeZoneId = timeZoneId;
		}

		public String getBusinessUnit() {
			return businessUnit;
		}

		public void setBusinessUnit(String businessUnit) {
			this.businessUnit = businessUnit;
		}

		public String getSiloStatus() {
			return siloStatus;
		}

		public void setSiloStatus(String siloStatus) {
			this.siloStatus = siloStatus;
		}

		public String getCostCenter() {
			return costCenter;
		}

		public void setCostCenter(String costCenter) {
			this.costCenter = costCenter;
		}

		public String getFinancialLocCode() {
			return financialLocCode;
		}

		public void setFinancialLocCode(String financialLocCode) {
			this.financialLocCode = financialLocCode;
		}
		
		public String getBusinessUnitName() {
			return businessUnitName;
		}

		public void setBusinessUnitName(String businessUnitName) {
			this.businessUnitName = businessUnitName;
		}

		public String getDepartmentName() {
			return departmentName;
		}

		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
		}

		public int compareTo(Object obj){
	      EPUser user = (EPUser)obj;

	      String c1 = getLastName() + getFirstName() + getMiddleInitial();
	      String c2 = user.getLastName() + user.getFirstName() + user.getMiddleInitial();

	      return c1.compareTo(c2);
	    }

		public boolean isOnline() {
			return online;
		}

		public void setOnline(boolean online) {
			this.online = online;
		}

		public String getChatId() {
			return chatId;
		}

		public void setChatId(String chatId) {
			this.chatId = chatId;
		}

		
	public void setPseudoEPRoles(SortedSet<EPRole> pseudoRoles) {
		this.pseudoRoles = pseudoRoles;
	}
	
	public SortedSet<EPUserApp> getEPUserApps() {
		return userApps;
	}

	public void setEPUserApps(SortedSet<EPUserApp> userApps) {
		this.userApps = userApps;
	}

	public void addUserApp(EPUserApp userApp) {
		userApps.add(userApp);
	}
	
	public void addAppRoles(EPApp app, SortedSet<EPRole> roles) {
		if (roles != null) {
			// add all
			SortedSet<EPUserApp> userApps = new TreeSet<EPUserApp>();
			// this.userApps.removeAll(this.userApps);
			Iterator<EPRole> itr = roles.iterator();
			while (itr.hasNext()) {
				EPRole role = (EPRole) itr.next();
				EPUserApp userApp = new EPUserApp();
				userApp.setUserId(this.id);
				userApp.setApp(app);
				userApp.setRole(role);
				userApps.add(userApp);
			}
			setEPUserApps(userApps);
		} else {
			// remove all
			setEPUserApps(null);
		}

	}

	public SortedSet<EPRole> getAppEPRoles(EPApp app) {
		
		logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - app = {}", app.getName());
		
		SortedSet<EPRole> roles = new TreeSet<EPRole>();
		SortedSet<EPUserApp> userAppRoles = getEPUserApps();
		
		logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - userApps = ", userAppRoles.size());

		Iterator<EPUserApp> userAppRolesIterator = userAppRoles.iterator();
		EPUserApp userAppRole = null;
		// getting default app
		while (userAppRolesIterator.hasNext()) {
			EPUserApp tempUserApp = (EPUserApp) userAppRolesIterator.next();
			if (tempUserApp.getApp().getId().equals(app.getId())) {

				logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - for user {}, found application {}", this.getFullName(), app.getName());
				
				userAppRole = tempUserApp;
				
				EPRole role = userAppRole.getRole();
				if(role.getActive()){
					logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - Role {} is active - adding for user {} and app {}", role.getName(), this.getFullName(), app.getName());
					roles.add((EPRole) role);
				}
				else{
					logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - Role {} is NOT active - NOT adding for user {} and app {}", role.getName(), this.getFullName(), app.getName());
				}				
			}
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - roles = ", roles.size());

		return roles;
	}

	
	/**
	 * Attention! Not for use in ONAP
	 */
	public SortedSet<EPRole> getAppRoles(EPApp app) {
		SortedSet<EPRole> roles = new TreeSet<EPRole>();
		SortedSet<EPUserApp> apps = getEPUserApps();
		Iterator<EPUserApp> appsItr = apps.iterator();
		EPUserApp userApp = null;
		// getting default app
		while (appsItr.hasNext()) {
			EPUserApp tempUserApp = (EPUserApp) appsItr.next();
			if (tempUserApp.getApp().getId().equals(app.getId())) {
				userApp = tempUserApp;
				roles.add((EPRole) userApp.getRole());
			}
		}
		return roles;
	}

	

	/**
	 * Attention! Not for use in ONAP
	 */
	public SortedSet<EPRole> getEPRoles() {
		EPApp app = new EPApp();
		app.setId(new Long(PortalConstants.PORTAL_APP_ID));
		app.setName(ECOMP_PORTAL_NAME);
		return getAppEPRoles(app);
	}

	/**
	 * Attention! Not for use in ONAP
	 */
	public void setEPRoles(SortedSet<EPRole> roles) {
		EPApp app = new EPApp();
		app.setId(new Long(PortalConstants.PORTAL_APP_ID));
		app.setName(ECOMP_PORTAL_NAME);
		addAppRoles(app, roles);
	}

	/**
	 * Attention! Not for use in ONAP
	 */
	public void removeEPRole(Long roleId) {
		SortedSet<EPUserApp> apps = getEPUserApps();
		Iterator<EPUserApp> appsItr = apps.iterator();
		// getting default app
		while (appsItr.hasNext()) {
			EPUserApp tempUserApp = (EPUserApp) appsItr.next();
			if (tempUserApp.equals(new Long(PortalConstants.PORTAL_APP_ID)) && tempUserApp.getRole().getId().equals(roleId)) {
				appsItr.remove();
			}
		}
	}

	/**
	 * Attention! Not for use in ONAP
	 */
	public void addEPRole(EPRole role) {
		if (role != null) {
			SortedSet<EPRole> roles = getEPRoles();
			if (roles == null) {
				roles = new TreeSet<EPRole>();
			}
			roles.add(role);
			setEPRoles(roles);
		}
	}
	
	public boolean isGuest() {
		return isGuest;
	}

	public void setGuest(boolean isGuest) {
		this.isGuest = isGuest;
	}

	@Override
	public String toString() {
		return "EPUser [orgId=" + orgId + ", managerId=" + managerId + ", firstName=" + firstName + ", middleInitial="
				+ middleInitial + ", lastName=" + lastName + ", phone=" + phone + ", fax=" + fax + ", cellular="
				+ cellular + ", email=" + email + ", addressId=" + addressId + ", alertMethodCd=" + alertMethodCd
				+ ", hrid=" + hrid + ", orgUserId=" + orgUserId + ", orgCode=" + orgCode + ", address1=" + address1
				+ ", address2=" + address2 + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode
				+ ", country=" + country + ", orgManagerUserId=" + orgManagerUserId + ", locationClli=" + locationClli
				+ ", businessCountryCode=" + businessCountryCode + ", businessCountryName=" + businessCountryName
				+ ", businessUnit=" + businessUnit + ", businessUnitName=" + businessUnitName + ", department="
				+ department + ", departmentName=" + departmentName + ", companyCode=" + companyCode + ", company="
				+ company + ", zipCodeSuffix=" + zipCodeSuffix + ", jobTitle=" + jobTitle + ", commandChain="
				+ commandChain + ", siloStatus=" + siloStatus + ", costCenter=" + costCenter + ", financialLocCode="
				+ financialLocCode + ", loginId=" + loginId + ", loginPwd=" + loginPwd + ", lastLoginDate="
				+ lastLoginDate + ", active=" + active + ", internal=" + internal + ", selectedProfileId="
				+ selectedProfileId + ", timeZoneId=" + timeZoneId + ", online=" + online + ", chatId=" + chatId
				+ ", isGuest=" + isGuest + ", userApps=" + userApps + ", pseudoRoles=" + pseudoRoles + "]";
	}
	
	
}
