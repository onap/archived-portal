/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
package org.onap.portalapp.portal.transport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalsdk.core.domain.support.DomainVo;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This is to handle portal admins
 */
@Entity 
@Table(name="fn_common_widget_data")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class CommonWidget extends DomainVo{

	private static final long serialVersionUID = 7897021982887364557L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "category")
	@Size(max = 32)
	@SafeHtml
	public String category;
	
	@Column(name = "href")
	@Size(max = 512)
	@SafeHtml
	public String href;

	@Column(name = "title")
	@Size(max = 256)
	@SafeHtml
	public String title;
	
	@Column(name = "content")
	@Size(max = 4096)
	@SafeHtml
	public String content;

	@Column(name = "event_date")
	@Size(max = 10)
	@Pattern(regexp = "([1-2][0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")
	@SafeHtml
	public String eventDate;
	
	@Column(name = "sort_order")
	public Integer sortOrder;


	public CommonWidget(String category, String href, String title, String content, String eventDate, Integer sortOrder){
		this.category = category;
		this.href = href;
		this.title = title;
		this.content = content;
		this.eventDate = eventDate;
		this.sortOrder = sortOrder;
	}

}
