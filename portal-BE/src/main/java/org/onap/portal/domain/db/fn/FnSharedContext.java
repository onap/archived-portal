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

package org.onap.portal.domain.db.fn;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.DomainVo;

/*
CREATE TABLE `fn_shared_context` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `create_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
        `context_id` varchar(64) NOT NULL,
        `ckey` varchar(128) NOT NULL,
        `cvalue` varchar(1024) DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `session_key` (`context_id`,`ckey`)
        )
*/

@NamedQueries({
    @NamedQuery(
        name = "FnSharedContext.getByContextId",
        query = "FROM FnSharedContext WHERE contextId = :contextId"),
    @NamedQuery(
        name = "FnSharedContext.getByContextIdAndCkey",
        query = "FROM FnSharedContext WHERE contextId = :contextId and ckey = :ckey")
})

@NamedNativeQueries({
    @NamedNativeQuery(
        name = "FnSharedContext.deleteByCreated",
        query = "delete FnSharedContext where created < :created"
    )
})

@Table(name = "fn_shared_context", uniqueConstraints ={
        @UniqueConstraint(columnNames = {"context_Id", "ckey"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FnSharedContext extends DomainVo implements Serializable {

       @Column(name = "context_id", length = 64, nullable = false)
       @Size(max = 64)
       @SafeHtml
       @NotNull
       private String contextId;
       @Column(name = "ckey", length = 128, nullable = false)
       @Size(max = 128)
       @SafeHtml
       @NotNull
       private String ckey;
       @Column(name = "cvalue", length = 1024, nullable = false)
       @Size(max = 1024)
       @SafeHtml
       @NotNull
       private String cvalue;

       @Builder
       public FnSharedContext(@Digits(integer = 11, fraction = 0) Long id,
           LocalDateTime created, LocalDateTime modified, Long rowNum, Serializable auditUserId,
           DomainVo createdId, DomainVo modifiedId, Set<DomainVo> fnUsersCreatedId,
           Set<DomainVo> fnUsersModifiedId,
           @Size(max = 64) @NotNull String contextId,
           @Size(max = 128) @NotNull String ckey,
           @Size(max = 1024) @NotNull String cvalue) {
              super(id, created, modified, rowNum, auditUserId, createdId, modifiedId, fnUsersCreatedId,
                  fnUsersModifiedId);
              this.contextId = contextId;
              this.ckey = ckey;
              this.cvalue = cvalue;
       }
}
