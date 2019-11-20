package org.onap.portal.dao.fn;

import java.util.List;
import org.onap.portal.domain.db.fn.FnApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FnAppDao extends JpaRepository<FnApp, Long> {

  @Query
  List<FnApp> getByUebKey(final @Param("uebKey") String uebKey);

}
