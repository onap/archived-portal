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

package org.onap.portal.domain.dto.transport;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CentralV2UserApp implements Serializable, Comparable {

    private static final long serialVersionUID = 4954830080839125389L;

    private Long userId;
    private CentralApp app;
    private CentralV2Role role;
    private Integer priority;

    public int compareTo(Object other) {
        CentralV2UserApp castOther = (CentralV2UserApp) other;

        Long c1 = (this.getUserId() == null ? 0 : this.getUserId()) + (this.priority == null ? 0 : this.priority);
        Long c2 = (castOther.getUserId() == null ? 0 : castOther.getUserId());
        c2 += (castOther.getApp() == null || castOther.getApp().getId() == null ? 0 : castOther.getApp().getId());
        c2 += (castOther.priority == null ? 0 : castOther.priority);

        return c1.compareTo(c2);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((app == null) ? 0 : app.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CentralV2UserApp)) {
            return false;
        }
        CentralV2UserApp castOther = (CentralV2UserApp) other;
        return Objects.equals(this.userId, castOther.userId) &&
                Objects.equals(this.app, castOther.app) &&
                Objects.equals(this.role, castOther.role) &&
                Objects.equals(this.priority, castOther.priority);
    }
}
