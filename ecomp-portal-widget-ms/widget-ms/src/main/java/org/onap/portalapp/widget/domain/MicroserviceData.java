package org.onap.portalapp.widget.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * TODO: moved all microservice-related code (domain, controller, service)
 * from onap portal Backend to widget microservice
 */
@Entity
@Table(name="EP_MICROSERVICE")
@Getter
@Setter
public class MicroserviceData {
	
	@Id
	@Column(name = "id")
	@Digits(integer = 11, fraction = 0)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	@Size(max = 50)
	@SafeHtml
	private String name;

	@Column(name = "description")
	@Size(max = 50)
	@SafeHtml
	private String desc;
	
	@Column(name = "appId")
	@Digits(integer = 11, fraction = 0)
	private long appId;

	@Column(name = "endpoint_url")
	@Size(max = 200)
	@SafeHtml
	private String url;

	@Column(name = "security_type")
	@Size(max = 50)
	@SafeHtml
	private String securityType;

	@Column(name = "username")
	@Size(max = 50)
	@SafeHtml
	private String username;

	@Column(name = "password")
	@Size(max = 50)
	@SafeHtml
	@NotNull
	private String password;
	
	@Column(name = "active")
	@Size(max = 1)
	@SafeHtml
	@NotNull
	private String active;

	@Override
	public String toString() {
		return "MicroserviceData [name=" + name + ", desc=" + desc + ", appId=" + appId + ", url=" + url
				+ ", securityType=" + securityType + ", username=" + username + ", password=" + password + ", active="
				+ active + "]";
	}
	
}
