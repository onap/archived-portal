package org.onap.portalapp.widget.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "FN_ROLE")
@Getter
@Setter
public class RoleApp implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Digits(integer = 11, fraction = 0)
	private Long roleId;

	@Column(name = "ROLE_Name")
	@SafeHtml
	@Size(max = 300)
	@NotNull
	private String roleName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="APP_ID")
	@Valid
	private App app;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy="widgetRoles")
	@Valid
	private Set<WidgetCatalog> widgets;

	@Override
	public String toString() {
		return "RoleApp [roleId=" + roleId + ", roleName=" + roleName + ", app=" + app + "]";
	}
	
}
