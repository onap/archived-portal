package org.onap.portalapp.widget.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * TODO: moved all microservice-related code (domain, controller, service)
 * from onap portal Backend to widget microservice
 */
@Entity
@Table(name="EP_MICROSERVICE_PARAMETER")
@Getter
@Setter
public class MicroserviceParameter {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Digits(integer = 11, fraction = 0)
	private Long id;

	@Column(name = "service_id")
	@Digits(integer = 11, fraction = 0)
	private long serviceId;

	@Column(name = "para_key")
	@Size(max = 50)
	@SafeHtml
	private String para_key;

	@Column(name = "para_value")
	@Size(max = 50)
	@SafeHtml
	private String para_value;

	@Override
	public String toString() {
		return "MicroserviceParameter [serviceId=" + serviceId + ", para_key=" + para_key + ", para_value=" + para_value
				+ "]";
	}
	
}
