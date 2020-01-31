package org.onap.portal.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.portal.domain.dto.transport.WidgetCatalogPersonalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:test.properties")
class PersUserWidgetServiceTest {

    @Autowired
    private PersUserWidgetService persUserWidgetService;

    @Test
    void setPersUserAppValueInvalidWidgetIdDataTest() {
        WidgetCatalogPersonalization catalog = getWidgetCatalog();
        catalog.setSelect(true);
        try {
            persUserWidgetService.setPersUserAppValue(1, catalog);
        }catch (IllegalArgumentException e){
            assertEquals("widgetId may not be null", e.getMessage());
        }
    }

    @Test
    void setPersUserAppValueInvalidSelectDataTest() {
        WidgetCatalogPersonalization catalog = getWidgetCatalog();
        catalog.setWidgetId(1L);
        try {
            persUserWidgetService.setPersUserAppValue(1, catalog);
        }catch (IllegalArgumentException e){
            assertEquals("select may not be null", e.getMessage());
        }
    }

    private WidgetCatalogPersonalization getWidgetCatalog(){
        return new WidgetCatalogPersonalization();
    }
}
