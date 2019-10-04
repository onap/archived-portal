package org.onap.portal.aop.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.onap.portal.domain.db.fn.FnUser;
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

       @Before("execution(* org.onap.portal.service.WidgetService.setOnboardingWidget(..)) && args(fnUser, onboardingWidget)")
       public void setOnboardingWidget(final FnUser fnUser, OnboardingWidget onboardingWidget) {
              if (!dataValidator.isValid(onboardingWidget)) {
                     throw new IllegalArgumentException(dataValidator.getConstraintViolationsString(onboardingWidget));
              }
       }
}
