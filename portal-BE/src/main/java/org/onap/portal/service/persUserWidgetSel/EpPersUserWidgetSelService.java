package org.onap.portal.service.persUserWidgetSel;

import org.onap.portal.domain.db.ep.EpPersUserWidgetSel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@EnableAspectJAutoProxy
public class EpPersUserWidgetSelService {

    private final EpPersUserWidgetSelDao epPersUserWidgetSelDao;

    @Autowired
    public EpPersUserWidgetSelService(final EpPersUserWidgetSelDao epPersUserWidgetSelDao) {
        this.epPersUserWidgetSelDao = epPersUserWidgetSelDao;
    }

    public void deleteById(final long id) {
        epPersUserWidgetSelDao.deleteById(id);
    }

    public EpPersUserWidgetSel saveAndFlush(final EpPersUserWidgetSel epPersUserWidgetSel) {
        return epPersUserWidgetSelDao.saveAndFlush(epPersUserWidgetSel);
    }

    public Optional<List<EpPersUserWidgetSel>> getEpPersUserWidgetSelForUserIdAndWidgetId(final long id, final long widgetId) {
        return epPersUserWidgetSelDao.getEpPersUserWidgetSelForUserIdAndWidgetId(id, widgetId);
    }
}
