package org.openecomp.portalapp.widget.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TODO: moved all microservice-related code (domain, controller, service)
 * from ecomp portal Backend to widget microservice
 */
@Entity
@Table(name="EP_MICROSERVICE_PARAMETER")
public class MicroserviceParameter {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name = "service_id")
	private long serviceId;

	@Column(name = "para_key")
	private String para_key;

	@Column(name = "para_value")
	private String para_value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public String getPara_key() {
		return para_key;
	}

	public void setPara_key(String para_key) {
		this.para_key = para_key;
	}

	public String getPara_value() {
		return para_value;
	}

	public void setPara_value(String para_value) {
		this.para_value = para_value;
	}

	@Override
	public String toString() {
		return "MicroserviceParameter [serviceId=" + serviceId + ", para_key=" + para_key + ", para_value=" + para_value
				+ "]";
	}
	
}
