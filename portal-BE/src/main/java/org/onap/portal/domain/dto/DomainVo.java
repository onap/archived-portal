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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portalsdk.core.domain.FusionVo;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
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
              Long c2 = ((org.onap.portalsdk.core.domain.support.DomainVo)obj).getId();
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
                     newVo = (DomainVo)ois.readObject();
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
}
