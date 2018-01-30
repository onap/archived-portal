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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.command;

import java.util.List;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalsdk.core.command.support.SearchBase;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class PostSearchBean extends SearchBase {

    private EPUser     user     = null;
    private EPUser     userOrig = null;
    private String[] selected;
    private String[] postHrid;
    private String[] postOrgUserId;
    private String[] postFirstName;
    private String[] postLastName;
    private String[] postOrgCode;
    private String[] postPhone;
    private String[] postEmail;
    private String[] postAddress1;
    private String[] postAddress2;
    private String[] postCity;
    private String[] postState;
    private String[] postZipCode;
    private String[] postLocationClli;
    private String[] postBusinessCountryCode;
    private String[] postBusinessCountryName;
    private String[] postDepartment;
    private String[] postDepartmentName;
    private String[] postBusinessUnit;
    private String[] postBusinessUnitName;
    private String[] postJobTitle;
    private String[] postOrgManagerUserId;
    private String[] postCommandChain;
    private String[] postCompanyCode;
    private String[] postCompany;
    private String[] postCostCenter;
    private String[] postSiloStatus;
    private String[] postFinancialLocCode;


    public PostSearchBean() {
      this(null);
    } // PostSearchBean

    public PostSearchBean(List<?> items) {
      super(items);

      user     = new EPUser();
      userOrig = new EPUser();

      setSortBy1("");
      setSortBy1Orig("");

      //setSortByList(...);
    }	// PostSearchBean


    public String getFirstName()                      { return user.getFirstName(); }
    public String getLastName()                       { return user.getLastName(); }
    public String getHrid()                           { return user.getHrid(); }
    public String getOrgUserId()                      { return user.getOrgUserId(); }
    public String getOrgCode()                        { return user.getOrgCode(); }
    public String getEmail()                          { return user.getEmail(); }
    public String getOrgManagerUserId()                  { return user.getOrgManagerUserId(); }
    
    public String getFirstNameOrig()                  { return user.getFirstName(); }
    public String getLastNameOrig()                   { return user.getLastName(); }
    public String getHridOrig()                       { return user.getHrid(); }
    public String getOrgUserIdOrig()                  { return user.getOrgUserId(); }
    public String getOrgCodeOrig()                    { return user.getOrgCode(); }
    public String getEmailOrig()                      { return user.getEmail(); }
    public String getOrgManagerUserIdOrig()              { return user.getOrgManagerUserId(); }
    
    
    public EPUser getUser()                             { return user; }

    public String[] getPostEmail() {
        return postEmail;
    }

    public String[] getPostFirstName() {
        return postFirstName;
    }

    public String[] getPostHrid() {
        return postHrid;
    }

    public String[] getPostLastName() {
        return postLastName;
    }

    public String[] getPostOrgCode() {
        return postOrgCode;
    }

    public String[] getPostPhone() {
        return postPhone;
    }

    public String[] getPostOrgUserId() {
        return postOrgUserId;
    }

    public String[] getSelected() {
        return selected;
    }

    public String[] getPostAddress1() {
        return postAddress1;
    }

    public String[] getPostBusinessCountryCode() {
        return postBusinessCountryCode;
    }

    public String[] getPostCity() {
        return postCity;
    }

    public String[] getPostCommandChain() {
        return postCommandChain;
    }

    public String[] getPostCompany() {
        return postCompany;
    }

    public String[] getPostCompanyCode() {
        return postCompanyCode;
    }

    public String[] getPostDepartment() {
        return postDepartment;
    }

    public String[] getPostDepartmentName() {
        return postDepartmentName;
    }

    public String[] getPostBusinessCountryName() {
        return postBusinessCountryName;
    }

    public String[] getPostJobTitle() {
        return postJobTitle;
    }

    public String[] getPostLocationClli() {
        return postLocationClli;
    }

    public String[] getPostManagerUserId() {
        return postOrgManagerUserId;
    }

    public String[] getPostState() {
        return postState;
    }

    public String[] getPostZipCode() {
        return postZipCode;
    }

    public void setFirstName(String value)            { user.setFirstName(value); }
    public void setLastName(String value)             { user.setLastName(value); }
    public void setHrid(String value)                 { user.setHrid(value); }
    public void setOrgUserId(String value)            { user.setOrgUserId(value); }
    public void setOrgCode(String value)              { user.setOrgCode(value); }
    public void setEmail(String value)                { user.setEmail(value); }
    public void setOrgManagerUserId(String value)     { user.setOrgManagerUserId(value); }
    
    public void setFirstNameOrig(String value)        { userOrig.setFirstName(value); }
    public void setLastNameOrig(String value)         { userOrig.setLastName(value); }
    public void setHridOrig(String value)             { userOrig.setHrid(value); }
    public void setOrgUserIdOrig(String value)        { userOrig.setOrgUserId(value); }
    public void setOrgCodeOrig(String value)          { userOrig.setOrgCode(value); }
    public void setEmailOrig(String value)            { userOrig.setEmail(value); }
    public void setOrgManagerUserIdOrig(String value) { userOrig.setOrgManagerUserId(value); }
    
    public void setUser(EPUser value)                   { this.user = value; }

    public void setPostEmail(String[] postEmail) {
        this.postEmail = postEmail;
    }

    public void setPostFirstName(String[] postFirstName) {
        this.postFirstName = postFirstName;
    }

    public void setPostHrid(String[] postHrid) {
        this.postHrid = postHrid;
    }

    public void setPostLastName(String[] postLastName) {
        this.postLastName = postLastName;
    }

    public void setPostOrgCode(String[] postOrgCode) {
        this.postOrgCode = postOrgCode;
    }

    public void setPostPhone(String[] postPhone) {
        this.postPhone = postPhone;
    }

    public void setPostUserId(String[] postOrgUserId) {
        this.postOrgUserId = postOrgUserId;
    }

    public void setSelected(String[] selected) {
        this.selected = selected;
    }

    public void setPostAddress1(String[] postAddress1) {
        this.postAddress1 = postAddress1;
    }

    public void setPostBusinessCountryCode(String[] postBusinessCountryCode) {
        this.postBusinessCountryCode = postBusinessCountryCode;
    }

    public void setPostCity(String[] postCity) {
        this.postCity = postCity;
    }

    public void setPostCommandChain(String[] postCommandChain) {
        this.postCommandChain = postCommandChain;
    }

    public void setPostCompany(String[] postCompany) {
        this.postCompany = postCompany;
    }

    public void setPostCompanyCode(String[] postCompanyCode) {
        this.postCompanyCode = postCompanyCode;
    }

    public void setPostDepartment(String[] postDepartment) {
        this.postDepartment = postDepartment;
    }

    public void setPostDepartmentName(String[] postDepartmentName) {
        this.postDepartmentName = postDepartmentName;
    }

    public void setPostBusinessCountryName(String[] postBusinessCountryName) {
        this.postBusinessCountryName = postBusinessCountryName;
    }

    public void setPostJobTitle(String[] postJobTitle) {
        this.postJobTitle = postJobTitle;
    }

    public void setPostLocationClli(String[] postLocationClli) {
        this.postLocationClli = postLocationClli;
    }

    public void setPostManagerUserId(String[] postOrgManagerUserId) {
        this.postOrgManagerUserId = postOrgManagerUserId;
    }

    public void setPostState(String[] postState) {
        this.postState = postState;
    }

    public void setPostZipCode(String[] postZipCode) {
        this.postZipCode = postZipCode;
    }
    
    public String[] getPostAddress2() {
		return postAddress2;
	}

	public void setPostAddress2(String[] postAddress2) {
		this.postAddress2 = postAddress2;
	}

	public EPUser getUserOrig() {
		return userOrig;
	}

	public void setUserOrig(EPUser userOrig) {
		this.userOrig = userOrig;
	}

	public String[] getPostBusinessUnit() {
		return postBusinessUnit;
	}

	public void setPostBusinessUnit(String[] postBusinessUnit) {
		this.postBusinessUnit = postBusinessUnit;
	}

	public String[] getPostBusinessUnitName() {
		return postBusinessUnitName;
	}

	public void setPostBusinessUnitName(String[] postBusinessUnitName) {
		this.postBusinessUnitName = postBusinessUnitName;
	}

	public String[] getPostCostCenter() {
		return postCostCenter;
	}

	public void setPostCostCenter(String[] postCostCenter) {
		this.postCostCenter = postCostCenter;
	}

	public String[] getPostSiloStatus() {
		return postSiloStatus;
	}

	public void setPostSiloStatus(String[] postSiloStatus) {
		this.postSiloStatus = postSiloStatus;
	}

	public String[] getPostFinancialLocCode() {
		return postFinancialLocCode;
	}

	public void setPostFinancialLocCode(String[] postFinancialLocCode) {
		this.postFinancialLocCode = postFinancialLocCode;
	}

	public void resetSearch() {
          super.resetSearch();
          setUser(new EPUser());
	} // resetSearch


	public boolean isCriteriaUpdated() {
          if(user==null&&userOrig==null)
            return false;
          else if(user==null||userOrig==null)
            return true;
          else
            return (! (
                Utilities.nvl(user.getFirstName()).equals(Utilities.nvl(userOrig.getFirstName()))&&
                Utilities.nvl(user.getLastName()).equals(Utilities.nvl(userOrig.getLastName()))&&
                //Utilities.nvl(user.getHrid()).equals(Utilities.nvl(userOrig.getHrid()))&&
                Utilities.nvl(user.getOrgUserId()).equals(Utilities.nvl(userOrig.getOrgUserId()))&&
                Utilities.nvl(user.getOrgCode()).equals(Utilities.nvl(userOrig.getOrgCode()))&&
                Utilities.nvl(user.getEmail()).equals(Utilities.nvl(userOrig.getEmail()))&&
                Utilities.nvl(user.getOrgManagerUserId()).equals(Utilities.nvl(userOrig.getOrgManagerUserId()))&&
                true));
       } // isCriteriaUpdated

}	// PostSearchBean
