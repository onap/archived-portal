/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.transport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openecomp.portalsdk.core.domain.support.DomainVo;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This is to handle portal admins
 * @author aw3218
 */
@Entity 
@Table(name="fn_common_widget_data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonWidget extends DomainVo{

	private static final long serialVersionUID = 7897021982887364557L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "category")
	public String category;
	
	@Column(name = "href")
	public String href;

	@Column(name = "title")
	public String title;
	
	@Column(name = "content")
	public String content;

	@Column(name = "event_date")
	public String eventDate;
	
	@Column(name = "sort_order")
	public Integer sortOrder;

	
	public CommonWidget(){
		
	}
	
	public CommonWidget(String category, String href, String title, String content, String eventDate, Integer sortOrder){
		this.category = category;
		this.href = href;
		this.title = title;
		this.content = content;
		this.eventDate = eventDate;
		this.sortOrder = sortOrder;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {		
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}	
}
