package org.onap.portal.aop.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.onap.portal.domain.db.fn.FnWidget;
import org.onap.portal.domain.dto.transport.OnboardingWidget;
import org.onap.portal.validation.DataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WidgetServiceAOP {
       private final Long LONG_ECOMP_APP_ID = 1L;

       private static final Logger LOGGER = LoggerFactory.getLogger(WidgetServiceAOP.class);

       private final DataValidator dataValidator;

       @Autowired
       public WidgetServiceAOP(DataValidator dataValidator) {
              this.dataValidator = dataValidator;
       }

       @Before("execution(* org.onap.portal.service.widget.WidgetService.setOnboardingWidget(..)) && args(userId, onboardingWidget)")
       public void setOnboardingWidget(final Long userId, OnboardingWidget onboardingWidget) {
              if (!dataValidator.isValid(onboardingWidget)) {
                     throw new IllegalArgumentException(dataValidator.getConstraintViolationsString(onboardingWidget));
              }
       }

       @Before("execution(* org.onap.portal.service.widget.WidgetService.saveOne(..)) && args(widget)")
       public void saveOne(final FnWidget widget) {
              if (!dataValidator.isValid(widget)) {
                     throw new IllegalArgumentException(dataValidator.getConstraintViolationsString(widget));
              }
       }
}
