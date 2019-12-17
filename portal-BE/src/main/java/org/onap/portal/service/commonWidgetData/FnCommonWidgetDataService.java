package org.onap.portal.service.commonWidgetData;

import org.onap.portal.domain.db.fn.FnCommonWidgetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnCommonWidgetDataService {

    private final FnCommonWidgetDataDao fnCommonWidgetDataDao;

    @Autowired
    public FnCommonWidgetDataService(FnCommonWidgetDataDao fnCommonWidgetDataDao) {
        this.fnCommonWidgetDataDao = fnCommonWidgetDataDao;
    }

    public List<FnCommonWidgetData> saveAll(List<FnCommonWidgetData> fnCommonWidgetDataList) {
        return fnCommonWidgetDataDao.saveAll(fnCommonWidgetDataList);
    }
}
