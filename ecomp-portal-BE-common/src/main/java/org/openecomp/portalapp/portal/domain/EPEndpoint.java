package org.openecomp.portalapp.portal.domain;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

public class EPEndpoint extends DomainVo {

	private static final long serialVersionUID = 1L;

	public EPEndpoint() {

	}

	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "EPEndpoint [id=" + id + ", name=" + name + "]";
	}

}
