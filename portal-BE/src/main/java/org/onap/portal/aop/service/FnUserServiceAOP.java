package org.onap.portal.aop.service;

import java.security.Principal;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.validation.DataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FnUserServiceAOP {
       private static final Logger LOGGER = LoggerFactory.getLogger(FnLanguageServiceAOP.class);

       @Autowired
       private DataValidator dataValidator;

       @Before("execution(* org.onap.portal.service.fn.FnUserService.saveFnUser(..)) && args(principal, fnUser)")
       public void save(final Principal principal, final FnUser fnUser) {
              if (fnUser == null) {
                     LOGGER.error("User " + principal.getName() + " try to save NULL fnUser");
                     throw new NullPointerException("FnUser cannot be null or empty");
              }
              if (!dataValidator.isValid(fnUser)) {
                     String violations = dataValidator.getConstraintViolations(fnUser).stream()
                             .map(ConstraintViolation::getMessage)
                             .collect(Collectors.joining(", "));
                     LOGGER.error("User " + principal.getName() + " try to save not valid fnUser: " + violations);
                     throw new IllegalArgumentException("FnUser is not valid, " + violations);
              }
       }
}
