package org.onap.portal.dao.fn;

import org.onap.portal.domain.db.fn.FnApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FnAppDao extends JpaRepository<FnApp, Long> {

}
