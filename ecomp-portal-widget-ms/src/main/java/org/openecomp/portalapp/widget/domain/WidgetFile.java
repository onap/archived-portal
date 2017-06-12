package org.openecomp.portalapp.widget.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ep_widget_catalog_files")
public class WidgetFile {
	@Id
	@Column (name = "file_id")
	private int id;
	
	@Column(name = "widget_name")
	private String name;
	
	@Column(name = "widget_id")
	private long widgetId;
	
	@Column(name = "markup_html")
	private byte[] markup;
	
	@Column(name = "controller_js")
	private  byte[] controller;
	
	@Column(name = "framework_js")
	private  byte[] framework;

	@Column(name = "widget_css")
	private  byte[] css;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getMarkup() {
		return markup;
	}

	public void setMarkup(byte[] markup) {
		this.markup = markup;
	}

	public byte[] getController() {
		return controller;
	}

	public void setController(byte[] controller) {
		this.controller = controller;
	}

	public byte[] getFramework() {
		return framework;
	}

	public void setFramework(byte[] framework) {
		this.framework = framework;
	}

	public byte[] getCss() {
		return css;
	}

	public void setCss(byte[] css) {
		this.css = css;
	}

	public long getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(long widgetId) {
		this.widgetId = widgetId;
	}

	@Override
	public String toString() {
		return "WidgetFile [name=" + name + ", widgetId=" + widgetId + "]";
	}
	
	
    
}
