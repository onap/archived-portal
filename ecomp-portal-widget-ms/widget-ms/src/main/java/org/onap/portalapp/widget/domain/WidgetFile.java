package org.onap.portalapp.widget.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "ep_widget_catalog_files")
@Getter
@Setter
public class WidgetFile {
	@Id
	@Column (name = "file_id")
	@Digits(integer = 11, fraction = 0)
	private int id;
	
	@Column(name = "widget_name")
	@Size(max = 11)
	@SafeHtml
	@NotNull
	private String name;
	
	@Column(name = "widget_id")
	@Digits(integer = 11, fraction = 0)
	private long widgetId;
	
	@Column(name = "markup_html")
	private byte[] markup;
	
	@Column(name = "controller_js")
	private  byte[] controller;
	
	@Column(name = "framework_js")
	private  byte[] framework;

	@Column(name = "widget_css")
	private  byte[] css;

	@Override
	public String toString() {
		return "WidgetFile [name=" + name + ", widgetId=" + widgetId + "]";
	}
	
	
    
}
