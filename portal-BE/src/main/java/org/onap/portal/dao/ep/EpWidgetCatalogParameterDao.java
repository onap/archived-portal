package org.onap.portal.dao.ep;

import java.util.List;
import java.util.Optional;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EpWidgetCatalogParameterDao extends JpaRepository<EpWidgetCatalogParameter, Long> {

       @Query
       Optional<List<EpWidgetCatalogParameter>> retrieveByParamId(@Param("PARAMID") Long paramId);

       @Query
       @Modifying
       void deleteWidgetCatalogParameter(@Param("PARAMID") Long paramId);

       @Query
       Optional<List<EpWidgetCatalogParameter>> getUserParamById(@Param("WIDGETID") Long widgetId,
               @Param("USERID") Long userId, @Param("PARAMID") Long paramId);
}
