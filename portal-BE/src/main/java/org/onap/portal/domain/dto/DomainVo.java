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

package org.onap.portal.domain.dto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portalsdk.core.domain.FusionVo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DomainVo extends FusionVo implements Serializable, Cloneable, Comparable {

       private static final long serialVersionUID = 1L;

       protected Long id;
       protected Date created;
       protected Date modified;
       protected FnUser createdId;
       protected FnUser modifiedId;
       protected Long rowNum;
       protected Serializable auditUserId;
       protected Set auditTrail = null;

       @Override
       public int compareTo(Object obj) {
              Long c1 = this.getId();
              Long c2 = ((org.onap.portalsdk.core.domain.support.DomainVo) obj).getId();
              return c1 != null && c2 != null ? c1.compareTo(c2) : 1;
       }

       public Object copy(boolean isIdNull) {
              ByteArrayOutputStream baos = null;
              ByteArrayInputStream bais = null;
              ObjectOutputStream oos = null;
              ObjectInputStream ois = null;
              DomainVo newVo = null;

              try {
                     baos = new ByteArrayOutputStream();
                     oos = new ObjectOutputStream(baos);
                     oos.writeObject(this);
                     bais = new ByteArrayInputStream(baos.toByteArray());
                     ois = new ObjectInputStream(bais);
                     newVo = (DomainVo) ois.readObject();
                     if (isIdNull) {
                            newVo.setId(null);
                     }
              } catch (Exception var8) {
                     var8.printStackTrace();
              }

              return newVo;
       }

       public Object clone() throws CloneNotSupportedException {
              return super.clone();
       }

       public boolean equals(Object other) {
              if (this == other) {
                     return true;
              } else if (other == null) {
                     return false;
              } else if (!(other instanceof DomainVo)) {
                     return false;
              } else {
                     DomainVo castOther = (DomainVo)other;
                     return this.getId().equals(castOther.getId())
                             && this.getCreated().equals(castOther.getCreated())
                             && this.getCreatedId().equals(castOther.getCreatedId())
                             && this.getModified().equals(castOther.getModified())
                             && this.getModifiedId() == castOther.getModifiedId();
              }
       }

}
