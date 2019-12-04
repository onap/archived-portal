/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portal.domain.db.ep;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.DomainVo;

/*
CREATE TABLE `ep_pers_user_widget_sel` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `user_id` int(11) NOT NULL,
        `widget_id` int(11) NOT NULL,
        `status_cd` char(1) NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `uk_1_ep_pers_user_widg_sel_user_widg` (`user_id`,`widget_id`),
        KEY `fk_2_ep_pers_user_wid_sel_ep_wid` (`widget_id`),
        CONSTRAINT `fk_1_ep_pers_user_wid_sel_fn_user` FOREIGN KEY (`user_id`) REFERENCES `fn_user` (`user_id`),
        CONSTRAINT `fk_2_ep_pers_user_wid_sel_ep_wid` FOREIGN KEY (`widget_id`) REFERENCES `ep_widget_catalog` (`widget_id`)
        )
*/
@NamedQueries({
        @NamedQuery(
                name = "EpPersUserWidgetSel.getEpPersUserWidgetSelForUserIdAndWidgetId",
                query = "FROM EpPersUserWidgetSel WHERE userId.id = :USERID and widgetId.widgetId = :WIDGETID")
})
@Table(name = "ep_pers_user_widget_sel", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "widget_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpPersUserWidgetSel extends DomainVo implements Serializable {

       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
       @JoinColumn(name = "user_id", nullable = false, columnDefinition = "bigint")
       @NotNull
       @Valid
       private FnUser userId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
       @JoinColumn(name = "widget_id", nullable = false)
       @NotNull
       @Valid
       private EpWidgetCatalog widgetId;
       @Column(name = "status_cd", length = 1, nullable = false)
       @Size(max = 1)
       @NotNull
       @SafeHtml
       private String statusCd;

}
