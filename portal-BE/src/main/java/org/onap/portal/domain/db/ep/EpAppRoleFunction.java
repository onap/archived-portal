package org.onap.portalapp.portal.domain.db.ep;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalapp.portal.domain.db.fn.FnApp;
import org.onap.portalapp.portal.domain.db.fn.FnRole;

@Table(name = "ep_app_role_function", indexes = {
        @Index(name = "UNIQUE KEY", columnList = "app_id, role_id, function_cd", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EpAppRoleFunction {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       @Column(name = "id", length = 11, nullable = false, columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
       @Digits(integer = 11, fraction = 0)
       private Integer id;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "app_id", insertable = false, updatable = false)
       @Valid
       @NotNull
       private FnApp appId;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumn(name = "role_id")
       @Valid
       @NotNull
       private FnRole fnRole;
       @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
       @JoinColumns({
               @JoinColumn(name = "app_id", referencedColumnName = "app_id"),
               @JoinColumn(name = "function_cd", referencedColumnName = "function_cd")
       })
       @Valid
       @NotNull
       private EpAppFunction epAppFunction;
       @Column(name = "role_app_id", length = 20, columnDefinition = "VARCHAR(20) DEFAULT NULL")
       @Digits(integer = 20, fraction = 0)
       @SafeHtml
       private String roleAppId;
}
