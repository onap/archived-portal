package org.onap.portalapp.widget.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;


@Entity
@Table(name="EP_WIDGET_CATALOG")
@Getter
@Setter
public class WidgetCatalog{
	
	@Id
	@Column(name = "widget_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Digits(integer = 11, fraction = 0)
	private long id;
	
	@Column(name = "wdg_name")
	@Size(max = 100)
	@SafeHtml
	@NotNull
	private String name;
	
	@Column(name = "wdg_desc")
	@Size(max = 200)
	@SafeHtml
	private String desc;	
	
	@Column(name = "wdg_file_loc")
	@Size(max = 256)
	@SafeHtml
	@NotNull
	private String fileLocation;
	
	@Column(name = "all_user_flag")
	@Size(max = 1)
	@SafeHtml
	@NotNull
	private String allowAllUser;
	
	@Column(name = "service_id")
	@Digits(integer = 11, fraction = 0)
	private Long serviceId;
	
	@Transient
	private String sortOrder;

	@Transient
	private String statusCode;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "ep_widget_catalog_role", 
			joinColumns = {@JoinColumn(name = "WIDGET_ID")},
			inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")}
	)
	private Set<RoleApp> widgetRoles;

	@Override
	public String toString() {
		return "WidgetCatalog [id=" + id + ", name=" + name + ", desc=" + desc + ", fileLocation=" + fileLocation
				+ ", allowAllUser=" + allowAllUser + ", serviceId=" + serviceId + ", sortOrder=" + sortOrder
				+ ", statusCode=" + statusCode + ", widgetRoles=" + widgetRoles + "]";
	}
}
