package org.openecomp.portalapp.portal.domain;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

public class EPEndpointAccount extends DomainVo {

	private static final long serialVersionUID = 1L;

	public EPEndpointAccount() {

	}

	private Long id;
	private Long ep_id;
	private Long account_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEp_id() {
		return ep_id;
	}

	public void setEp_id(Long ep_id) {
		this.ep_id = ep_id;
	}

	public Long getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}

	@Override
	public String toString() {
		return "EPEndpointAccount [id=" + id + ", ep_id=" + ep_id + ", account_id=" + account_id + "]";
	}

}
