/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 *
 */

package org.onap.portal.configuration;

import org.onap.portal.domain.db.ep.*;
import org.onap.portal.domain.db.fn.*;
import org.onap.portal.service.app.FnAppService;
import org.onap.portal.service.appContactUs.FnAppContactUsService;
import org.onap.portal.service.appFunction.EpAppFunctionService;
import org.onap.portal.service.appRoleFunction.EpAppRoleFunctionService;
import org.onap.portal.service.auditLog.FnAuditLogService;
import org.onap.portal.service.basicAuthAccount.EpBasicAuthAccountService;
import org.onap.portal.service.commonWidgetData.FnCommonWidgetDataService;
import org.onap.portal.service.displayText.FnDisplayTextService;
import org.onap.portal.service.function.FnFunctionService;
import org.onap.portal.service.language.FnLanguageService;
import org.onap.portal.service.luActivity.FnLuActivityService;
import org.onap.portal.service.luAlertMethod.FnLuAlertMethodService;
import org.onap.portal.service.luMenuSet.FnLuMenuSetService;
import org.onap.portal.service.luPriority.FnLuPriorityService;
import org.onap.portal.service.luTabSet.FnLuTabSetService;
import org.onap.portal.service.luTimezone.FnLuTimezoneService;
import org.onap.portal.service.menu.FnMenuService;
import org.onap.portal.service.menuFunctional.FnMenuFunctionalService;
import org.onap.portal.service.menuFunctionalAncestors.FnMenuFunctionalAncestorsService;
import org.onap.portal.service.menuFunctionalRoles.FnMenuFunctionalRolesService;
import org.onap.portal.service.microservice.EpMicroserviceService;
import org.onap.portal.service.microserviceParameter.EpMicroserviceParameterService;
import org.onap.portal.service.persUserAppSel.FnPersUserAppSelService;
import org.onap.portal.service.persUserAppSort.EpPersUserAppSortService;
import org.onap.portal.service.qzCronTriggers.FnQzCronTriggersService;
import org.onap.portal.service.qzJobDetails.FnQzJobDetailsService;
import org.onap.portal.service.qzLocks.FnQzLocksService;
import org.onap.portal.service.qzSchedulerState.FnQzSchedulerStateService;
import org.onap.portal.service.qzTriggers.FnQzTriggersService;
import org.onap.portal.service.restrictedUrl.FnRestrictedUrlService;
import org.onap.portal.service.role.FnRoleService;
import org.onap.portal.service.roleComposite.FnRoleCompositeService;
import org.onap.portal.service.roleFunction.FnRoleFunctionService;
import org.onap.portal.service.sharedContext.FnSharedContextService;
import org.onap.portal.service.tab.FnTabService;
import org.onap.portal.service.tabSelected.FnTabSelectedService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.service.userRole.FnUserRoleService;
import org.onap.portal.service.widgetCatalog.EpWidgetCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DBInitializingBean implements org.springframework.beans.factory.InitializingBean {

  private final FnAppService fnAppService;
  private final EpAppFunctionService epAppFunctionService;
  private final FnRoleService fnRoleService;
  private final EpAppRoleFunctionService epAppRoleFunctionService;
  private final EpBasicAuthAccountService epBasicAuthAccountService;
  private final EpMicroserviceService epMicroserviceService;
  private final EpMicroserviceParameterService epMicroserviceParameterService;
  private final EpWidgetCatalogService epWidgetCatalogService;
  private final FnAppContactUsService fnAppContactUsService;
  private final FnCommonWidgetDataService fnCommonWidgetDataService;
  private final FnLanguageService fnLanguageService;
  private final FnDisplayTextService fnDisplayTextService;
  private final FnFunctionService fnFunctionService;
  private final FnLuActivityService fnLuActivityService;
  private final FnLuAlertMethodService fnLuAlertMethodService;
  private final FnLuMenuSetService fnLuMenuSetService;
  private final FnLuPriorityService fnLuPriorityService;
  private final FnLuTimezoneService fnLuTimezoneService;
  private final FnMenuService fnMenuService;
  private final FnMenuFunctionalService fnMenuFunctionalService;
  private final FnMenuFunctionalAncestorsService fnMenuFunctionalAncestorsService;
  private final FnMenuFunctionalRolesService fnMenuFunctionalRolesService;
  private final FnQzJobDetailsService fnQzJobDetailsService;
  private final FnQzLocksService fnQzLocksService;
  private final FnQzSchedulerStateService fnQzSchedulerStateService;
  private final FnQzTriggersService fnQzTriggersService;
  private final FnQzCronTriggersService fnQzCronTriggersService;
  private final FnRestrictedUrlService fnRestrictedUrlService;
  private final FnRoleCompositeService fnRoleCompositeService;
  private final FnRoleFunctionService fnRoleFunctionService;
  private final FnSharedContextService fnSharedContextService;
  private final FnLuTabSetService fnLuTabSetService;
  private final FnTabService fnTabService;
  private final FnTabSelectedService fnTabSelectedService;
  private final FnUserService fnUserService;
  private final EpPersUserAppSortService epPersUserAppSortService;
  private final FnPersUserAppSelService fnPersUserAppSelService;
  private final FnAuditLogService fnAuditLogService;
  private final FnUserRoleService fnUserRoleService;

  @Autowired
  public DBInitializingBean(FnAppService fnAppService, EpAppFunctionService epAppFunctionService, FnRoleService fnRoleService, EpAppRoleFunctionService epAppRoleFunctionService, EpBasicAuthAccountService epBasicAuthAccountService, EpMicroserviceService epMicroserviceService, EpMicroserviceParameterService epMicroserviceParameterService, EpWidgetCatalogService epWidgetCatalogService, FnAppContactUsService fnAppContactUsService, FnCommonWidgetDataService fnCommonWidgetDataService, FnLanguageService fnLanguageService, FnDisplayTextService fnDisplayTextService, FnFunctionService fnFunctionService, FnLuActivityService fnLuActivityService, FnLuAlertMethodService fnLuAlertMethodService, FnLuMenuSetService fnLuMenuSetService, FnLuPriorityService fnLuPriorityService, FnLuTimezoneService fnLuTimezoneService, FnMenuService fnMenuService, FnMenuFunctionalService fnMenuFunctionalService, FnMenuFunctionalAncestorsService fnMenuFunctionalAncestorsService, FnMenuFunctionalRolesService fnMenuFunctionalRolesService, FnQzJobDetailsService fnQzJobDetailsService, FnQzLocksService fnQzLocksService, FnQzSchedulerStateService fnQzSchedulerStateService, FnQzTriggersService fnQzTriggersService, FnQzCronTriggersService fnQzCronTriggersService, FnRestrictedUrlService fnRestrictedUrlService, FnRoleCompositeService fnRoleCompositeService, FnRoleFunctionService fnRoleFunctionService, FnSharedContextService fnSharedContextService, FnLuTabSetService fnLuTabSetService, FnTabService fnTabService, FnTabSelectedService fnTabSelectedService, FnUserService fnUserService, EpPersUserAppSortService epPersUserAppSortService, FnPersUserAppSelService fnPersUserAppSelService, FnAuditLogService fnAuditLogService, FnUserRoleService fnUserRoleService) {
    this.fnAppService = fnAppService;
    this.epAppFunctionService = epAppFunctionService;
    this.fnRoleService = fnRoleService;
    this.epAppRoleFunctionService = epAppRoleFunctionService;
    this.epBasicAuthAccountService = epBasicAuthAccountService;
    this.epMicroserviceService = epMicroserviceService;
    this.epMicroserviceParameterService = epMicroserviceParameterService;
    this.epWidgetCatalogService = epWidgetCatalogService;
    this.fnAppContactUsService = fnAppContactUsService;
    this.fnCommonWidgetDataService = fnCommonWidgetDataService;
    this.fnLanguageService = fnLanguageService;
    this.fnDisplayTextService = fnDisplayTextService;
    this.fnFunctionService = fnFunctionService;
    this.fnLuActivityService = fnLuActivityService;
    this.fnLuAlertMethodService = fnLuAlertMethodService;
    this.fnLuMenuSetService = fnLuMenuSetService;
    this.fnLuPriorityService = fnLuPriorityService;
    this.fnLuTimezoneService = fnLuTimezoneService;
    this.fnMenuService = fnMenuService;
    this.fnMenuFunctionalService = fnMenuFunctionalService;
    this.fnMenuFunctionalAncestorsService = fnMenuFunctionalAncestorsService;
    this.fnMenuFunctionalRolesService = fnMenuFunctionalRolesService;
    this.fnQzJobDetailsService = fnQzJobDetailsService;
    this.fnQzLocksService = fnQzLocksService;
    this.fnQzSchedulerStateService = fnQzSchedulerStateService;
    this.fnQzTriggersService = fnQzTriggersService;
    this.fnQzCronTriggersService = fnQzCronTriggersService;
    this.fnRestrictedUrlService = fnRestrictedUrlService;
    this.fnRoleCompositeService = fnRoleCompositeService;
    this.fnRoleFunctionService = fnRoleFunctionService;
    this.fnSharedContextService = fnSharedContextService;
    this.fnLuTabSetService = fnLuTabSetService;
    this.fnTabService = fnTabService;
    this.fnTabSelectedService = fnTabSelectedService;
    this.fnUserService = fnUserService;
    this.epPersUserAppSortService = epPersUserAppSortService;
    this.fnPersUserAppSelService = fnPersUserAppSelService;
    this.fnAuditLogService = fnAuditLogService;
    this.fnUserRoleService = fnUserRoleService;
  }

  @Override
  public void afterPropertiesSet() {
    initAllTablesTable();
  }

  private void initAllTablesTable() {

    // FN_APP TABLE

    FnApp app = FnApp.builder()
        .appName("Default")
        .appImageUrl("assets/images/tmp/portal1.png")
        .appDescription("Some Default Description")
        .appNotes("Some Default Note")
        .appUrl("http://localhost")
        .appAlternateUrl("http://localhost")
        .appRestEndpoint("http://localhost:8080/ecompportal")
        .mlAppName("EcompPortal")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("dR2NABMkxPaFbIbym87ZwQ==")
        .open(false)
        .enabled(false)
        .activeYn(true)
        .appUsername("m00468@portal.onap.org")
        .uebKey("EkrqsjQqZt4ZrPh6'")
        .uebSecret(null)
        .uebTopicName(null)
        .appType(1L)
        .authCentral(true)
        .authNamespace("org.onap.portal")
        .build();
    FnApp app2 = FnApp.builder()
        .appName("xDemo App")
        .appImageUrl("images/cache/portal-222865671_37476.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("http://portal-sdk.simpledemo.onap.org:30212/ONAPPORTALSDK/welcome.htm")
        .appAlternateUrl(null)
        .appRestEndpoint("http://portal-sdk:8080/ONAPPORTALSDK/api/v3")
        .mlAppName("")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("2VxipM8Z3SETg32m3Gp0FvKS6zZ2uCbCw46WDyK6T5E=")
        .open(false)
        .enabled(true)
        .activeYn(true)
        .appUsername("Default")
        .uebKey("ueb_key")
        .uebSecret("ueb_secret")
        .uebTopicName("ECOMP-PORTAL-OUTBOX")
        .appType(1L)
        .authCentral(false)
        .authNamespace(null)
        .build();
    FnApp app3 = FnApp.builder()
        .appName("DMaaP Bus Ctrl")
        .appImageUrl("images/cache/portal944583064_80711.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("http://dmaap-bc.simpledemo.onap.org:/ECOMPDBCAPP/dbc#/dmaap")
        .appAlternateUrl(null)
        .appRestEndpoint("http://dmaap-bc:8989/ECOMPDBCAPP/api/v2")
        .mlAppName("")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("okYTaDrhzibcbGVq5mjkVQ==")
        .open(false)
        .enabled(false)
        .activeYn(true)
        .appUsername("Default")
        .uebKey("ueb_key")
        .uebSecret("ueb_secret")
        .uebTopicName("ECOMP-PORTAL-OUTBOX")
        .appType(1L)
        .authCentral(false)
        .authNamespace(null)
        .build();
    FnApp app4 = FnApp.builder()
        .appName("SDC")
        .appImageUrl("images/cache/portal956868231_53879.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("http://sdc.api.fe.simpledemo.onap.org:30206/sdc1/portal")
        .appAlternateUrl(null)
        .appRestEndpoint("http://sdc-be:8080/api/v3")
        .mlAppName("")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("j85yNhyIs7zKYbR1VlwEfNhS6b7Om4l0Gx5O8931sCI=")
        .open(false)
        .enabled(true)
        .activeYn(true)
        .appUsername("sdc")
        .uebKey("ueb_key")
        .uebSecret("ueb_secret")
        .uebTopicName("ECOMP-PORTAL-OUTBOX")
        .appType(1L)
        .authCentral(true)
        .authNamespace("org.onap.sdc")
        .build();
    FnApp app5 = FnApp.builder()
        .appName("Policy")
        .appImageUrl("images/cache/portal1470452815_67021.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("https://policy.api.simpledemo.onap.org:30219/onap/policy")
        .appAlternateUrl(null)
        .appRestEndpoint("https://pap:8443/onap/api/v3")
        .mlAppName("")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("2VxipM8Z3SETg32m3Gp0FvKS6zZ2uCbCw46WDyK6T5E=")
        .open(false)
        .enabled(true)
        .activeYn(true)
        .appUsername("Default")
        .uebKey("ueb_key_5")
        .uebSecret("ueb_secret")
        .uebTopicName("ECOMP-PORTAL-OUTBOX")
        .appType(1L)
        .authCentral(true)
        .authNamespace("org.onap.policy")
        .build();
    FnApp app6 = FnApp.builder()
        .appName("Virtual Infrastructure Deployment")
        .appImageUrl("images/cache/portal-345993588_92550.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("https://vid.api.simpledemo.onap.org:30200/vid/welcome.htm")
        .appAlternateUrl(null)
        .appRestEndpoint("https://vid:8443/vid/api/v3")
        .mlAppName("")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("2VxipM8Z3SETg32m3Gp0FvKS6zZ2uCbCw46WDyK6T5E=")
        .open(false)
        .enabled(true)
        .activeYn(true)
        .appUsername("Default")
        .uebKey("2Re7Pvdkgw5aeAUD")
        .uebSecret("S31PrbOzGgL4hg4owgtx47Da")
        .uebTopicName("ECOMP-PORTAL-OUTBOX-90")
        .appType(1L)
        .authCentral(true)
        .authNamespace("org.onap.vid")
        .build();
    FnApp app7 = FnApp.builder()
        .appName("A&AI UI")
        .appImageUrl("images/cache/portal-345993588_92550.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html#/viewInspect")
        .appAlternateUrl(null)
        .appRestEndpoint("https://aai-sparky-be.onap:8000/api/v2")
        .mlAppName("")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("4LK69amiIFtuzcl6Gsv97Tt7MLhzo03aoOx7dTvdjKQ=")
        .open(false)
        .enabled(true)
        .activeYn(true)
        .appUsername("aaiui")
        .uebKey("ueb_key_7")
        .uebSecret("ueb_secret")
        .uebTopicName("ECOMP-PORTAL-OUTBOX")
        .appType(1L)
        .authCentral(true)
        .authNamespace("org.onap.aai")
        .build();
    FnApp app8 = FnApp.builder()
        .appName("CLI")
        .appImageUrl("images/cache/portal-345993588_92550.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("http://cli.api.simpledemo.onap.org:30260/")
        .appAlternateUrl(null)
        .appRestEndpoint(null)
        .mlAppName("")
        .mlAppAdminId("?")
        .motsId(null)
        .appPassword("")
        .open(false)
        .enabled(false)
        .activeYn(true)
        .appUsername("")
        .uebKey("")
        .uebSecret("")
        .uebTopicName("")
        .appType(1L)
        .authCentral(false)
        .authNamespace(null)
        .build();
    FnApp app9 = FnApp.builder()
        .appName("MSB")
        .appImageUrl("images/cache/portal-345993588_92550.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("http://msb.api.simpledemo.onap.org:30280/iui/microservices/default.html")
        .appAlternateUrl(null)
        .appRestEndpoint(null)
        .mlAppName("")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("")
        .open(true)
        .enabled(true)
        .activeYn(true)
        .appUsername("")
        .uebKey("")
        .uebSecret("")
        .uebTopicName("")
        .appType(2L)
        .authCentral(false)
        .authNamespace(null)
        .build();
    FnApp app10 = FnApp.builder()
        .appName("MSB")
        .appImageUrl("images/cache/portal-345993588_92550.png")
        .appDescription(null)
        .appNotes(null)
        .appUrl("http://msb.api.simpledemo.onap.org:30280/iui/microservices/default.html")
        .appAlternateUrl(null)
        .appRestEndpoint(null)
        .mlAppName("")
        .mlAppAdminId("")
        .motsId(null)
        .appPassword("")
        .open(true)
        .enabled(true)
        .activeYn(true)
        .appUsername("")
        .uebKey("")
        .uebSecret("")
        .uebTopicName("")
        .appType(2L)
        .authCentral(false)
        .authNamespace(null)
        .build();
    List<FnApp> fnApps = new ArrayList<>(Arrays.asList(app, app2, app3, app4, app5, app6, app7, app8, app9, app10));
    fnAppService.saveAll(fnApps);

    // EP_APP_FUNCTION TABLE

    EpAppFunction function1 = EpAppFunction.builder().appId(app).functionCd("menu|menu_acc_admin|*")
        .functionName("Admin Account Menu'").build();
    EpAppFunction function2 = EpAppFunction.builder().appId(app).functionCd("menu|menu_admin|*")
        .functionName("Admin Menu").build();
    EpAppFunction function3 = EpAppFunction.builder().appId(app).functionCd("menu|menu_home|*")
        .functionName("Home Menu").build();
    EpAppFunction function4 = EpAppFunction.builder().appId(app).functionCd("menu|menu_logout|*")
        .functionName("Logout Menu").build();
    EpAppFunction function5 = EpAppFunction.builder().appId(app).functionCd("menu|menu_web_analytics|*")
        .functionName("Web Analytics").build();
    EpAppFunction function6 = EpAppFunction.builder().appId(app).functionCd("url|addWebAnalyticsReport|*")
        .functionName("Add Web Analytics Report").build();
    EpAppFunction function7 = EpAppFunction.builder().appId(app).functionCd("url|appsFullList|*")
        .functionName("Apps Full List").build();
    EpAppFunction function8 = EpAppFunction.builder().appId(app).functionCd("url|centralizedApps|*")
        .functionName("Centralized Apps").build();
    EpAppFunction function9 = EpAppFunction.builder().appId(app).functionCd("url|edit_notification|*")
        .functionName("User Notification").build();
    EpAppFunction function10 = EpAppFunction.builder().appId(app).functionCd("url|functionalMenu|*")
        .functionName("Functional Menu").build();
    EpAppFunction function11 = EpAppFunction.builder().appId(app).functionCd("url|getAdminNotifications|*")
        .functionName("Admin Notifications").build();
    EpAppFunction function12 = EpAppFunction.builder().appId(app).functionCd("url|getAllWebAnalytics|*")
        .functionName("Get All Web Analytics").build();
    EpAppFunction function13 = EpAppFunction.builder().appId(app).functionCd("url|getFunctionalMenuRole|*")
        .functionName("Get Functional Menu Role").build();
    EpAppFunction function14 = EpAppFunction.builder().appId(app).functionCd("url|getNotificationAppRoles|*")
        .functionName("Get Notification App Roles").build();
    EpAppFunction function15 = EpAppFunction.builder().appId(app).functionCd("url|getUserAppsWebAnalytics|*")
        .functionName("Get User Apps Web Analytics").build();
    EpAppFunction function16 = EpAppFunction.builder().appId(app).functionCd("url|getUserJourneyAnalyticsReport|*")
        .functionName("Get User Journey Report").build();
    EpAppFunction function17 = EpAppFunction.builder().appId(app).functionCd("url|get_roles%2f%2a|*")
        .functionName("getRolesOfApp").build();
    EpAppFunction function18 = EpAppFunction.builder().appId(app).functionCd("url|get_role_functions%2f%2a|*")
        .functionName("Get Role Functions").build();
    EpAppFunction function19 = EpAppFunction.builder().appId(app).functionCd("url|login|*").functionName("Login")
        .build();
    EpAppFunction function20 = EpAppFunction.builder().appId(app).functionCd("url|notification_code|*")
        .functionName("Notification Code").build();
    EpAppFunction function21 = EpAppFunction.builder().appId(app)
        .functionCd("url|role_function_list%2fsaveRoleFunction%2f%2a|*").functionName("Save Role Function").build();
    EpAppFunction function22 = EpAppFunction.builder().appId(app).functionCd("url|saveNotification|*")
        .functionName("publish notifications").build();
    EpAppFunction function23 = EpAppFunction.builder().appId(app).functionCd("url|syncRoles|*")
        .functionName("SyncRoles").build();
    EpAppFunction function24 = EpAppFunction.builder().appId(app).functionCd("url|url_role.htm|*")
        .functionName("role page").build();
    EpAppFunction function25 = EpAppFunction.builder().appId(app).functionCd("url|url_welcome.htm|*")
        .functionName("welcome page").build();
    EpAppFunction function26 = EpAppFunction.builder().appId(app).functionCd("url|userAppRoles|*")
        .functionName("userAppRoles").build();
    EpAppFunction function27 = EpAppFunction.builder().appId(app).functionCd("url|userApps|*")
        .functionName("User Apps").build();
    List<EpAppFunction> epAppFunctions = new ArrayList<>(Arrays
        .asList(function1, function2, function3, function4, function5, function6, function7, function8, function9,
            function10, function11, function12, function13, function14, function15, function16, function17, function18,
            function19, function20, function21, function22, function23, function24, function25, function26,
            function27));
    epAppFunctionService.saveAll(epAppFunctions);

    // FN_ROLE TABLE

    // TODO should he change .appId(app2.getId()) to .appId(app2)??
    // what that .appRoleId(0L) stands for? should we connect this fields with some field from FnApp?

    FnRole fnRole1 = FnRole.builder().roleName("System_Administrator").activeYn(true).priority(1).appId(null).appRoleId(null).build();
    FnRole fnRole16 = FnRole.builder().roleName("Standard_User").activeYn(true).priority(5).appId(null).appRoleId(null).build();
    FnRole fnRole900 = FnRole.builder().roleName("Restricted_App_Role").activeYn(true).priority(1).appId(null).appRoleId(null).build();
    FnRole fnRole950 = FnRole.builder().roleName("Portal_Notification_Admin").activeYn(true).priority(1).appId(null).appRoleId(null).build();
    FnRole fnRole999 = FnRole.builder().roleName("Account_Administrator").activeYn(true).priority(1).appId(null).appRoleId(null).build();
    FnRole fnRole1000 = FnRole.builder().roleName("System_Administrator").activeYn(true).priority(1).appId(app2.getId()).appRoleId(1L).build();
    FnRole fnRole1001 = FnRole.builder().roleName("Standard_User").activeYn(true).priority(1).appId(app2.getId()).appRoleId(16L).build();
    FnRole fnRole1002 = FnRole.builder().roleName("System_Administrator").activeYn(true).priority(1).appId(app3.getId()).appRoleId(1L).build();
    FnRole fnRole1003 = FnRole.builder().roleName("Standard_User").activeYn(true).priority(1).appId(app3.getId()).appRoleId(16L).build();
    FnRole fnRole1004 = FnRole.builder().roleName("ADMIN").activeYn(true).priority(1).appId(app4.getId()).appRoleId(0L).build();
    FnRole fnRole1005 = FnRole.builder().roleName("TESTOR").activeYn(true).priority(1).appId(app4.getId()).appRoleId(1L).build();
    FnRole fnRole1006 = FnRole.builder().roleName("System_Administrator").activeYn(true).priority(1).appId(app5.getId()).appRoleId(1L).build();
    FnRole fnRole1007 = FnRole.builder().roleName("Standard_User").activeYn(true).priority(1).appId(app5.getId()).appRoleId(16L).build();
    FnRole fnRole1008 = FnRole.builder().roleName("System_Administrator").activeYn(true).priority(1).appId(app6.getId()).appRoleId(1L).build();
    FnRole fnRole1009 = FnRole.builder().roleName("Standard_User").activeYn(true).priority(1).appId(app6.getId()).appRoleId(16L).build();
    FnRole fnRole1010 = FnRole.builder().roleName("Usage_Analyst").activeYn(true).priority(1).appId(null).appRoleId(null).build();
    FnRole fnRole1011 = FnRole.builder().roleName("View").activeYn(true).priority(1).appId(app7.getId()).appRoleId(1L).build();
    FnRole fnRole1012 = FnRole.builder().roleName("Standard_User").activeYn(true).priority(1).appId(app7.getId()).appRoleId(16L).build();
    FnRole fnRole2115 = FnRole.builder().roleName("Portal_Usage_Analyst").activeYn(true).priority(6).appId(null).appRoleId(null).build();

    List<FnRole> fnRoles = new ArrayList<>(Arrays
        .asList(fnRole1, fnRole16, fnRole900, fnRole950, fnRole999, fnRole1000, fnRole1001, fnRole1002, fnRole1003, fnRole1004, fnRole1005,
            fnRole1006, fnRole1007, fnRole1008, fnRole1009, fnRole1010, fnRole1011, fnRole1012, fnRole2115));
    fnRoleService.saveAll(fnRoles);

    // EP_APP_ROLE_FUNCTION TABLE

    EpAppRoleFunction epAppRoleFunction1 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1)
        .epAppFunction(function19).build();
    EpAppRoleFunction epAppRoleFunction2 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1)
        .epAppFunction(function2).build();
    EpAppRoleFunction epAppRoleFunction3 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1)
        .epAppFunction(function3).build();
    EpAppRoleFunction epAppRoleFunction4 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1)
        .epAppFunction(function4).build();
    EpAppRoleFunction epAppRoleFunction5 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1010)
        .epAppFunction(function19).build();
    EpAppRoleFunction epAppRoleFunction6 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1010)
        .epAppFunction(function3).build();
    EpAppRoleFunction epAppRoleFunction7 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1010)
        .epAppFunction(function4).build();
    EpAppRoleFunction epAppRoleFunction8 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole950)
        .epAppFunction(function9).build();
    EpAppRoleFunction epAppRoleFunction9 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole950)
        .epAppFunction(function11).build();
    EpAppRoleFunction epAppRoleFunction10 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole950)
        .epAppFunction(function22).build();
    EpAppRoleFunction epAppRoleFunction11 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function26).build();
    EpAppRoleFunction epAppRoleFunction12 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function11).build();
    EpAppRoleFunction epAppRoleFunction13 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function27).build();
    EpAppRoleFunction epAppRoleFunction14 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1010)
        .epAppFunction(function5).build();
    EpAppRoleFunction epAppRoleFunction15 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole2115)
        .epAppFunction(function5).build();
    EpAppRoleFunction epAppRoleFunction16 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole1)
        .epAppFunction(function1).build();
    EpAppRoleFunction epAppRoleFunction17 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function1).build();
    EpAppRoleFunction epAppRoleFunction18 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function8).build();
    EpAppRoleFunction epAppRoleFunction19 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function12).build();
    EpAppRoleFunction epAppRoleFunction20 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function13).build();
    EpAppRoleFunction epAppRoleFunction21 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function14).build();
    EpAppRoleFunction epAppRoleFunction22 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function15).build();
    EpAppRoleFunction epAppRoleFunction23 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function16).build();
    EpAppRoleFunction epAppRoleFunction24 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function17).build();
    EpAppRoleFunction epAppRoleFunction25 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function18).build();
    EpAppRoleFunction epAppRoleFunction26 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function20).build();
    EpAppRoleFunction epAppRoleFunction27 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function21).build();
    EpAppRoleFunction epAppRoleFunction28 = EpAppRoleFunction.builder().appId(app).fnRole(fnRole999)
        .epAppFunction(function23).build();

    List<EpAppRoleFunction> epAppRoleFunctions = new ArrayList<>(Arrays
        .asList(epAppRoleFunction1, epAppRoleFunction2, epAppRoleFunction3, epAppRoleFunction4, epAppRoleFunction5,
            epAppRoleFunction6, epAppRoleFunction7, epAppRoleFunction8, epAppRoleFunction9, epAppRoleFunction10,
            epAppRoleFunction11, epAppRoleFunction12, epAppRoleFunction13, epAppRoleFunction14, epAppRoleFunction15,
            epAppRoleFunction16, epAppRoleFunction17, epAppRoleFunction18, epAppRoleFunction19, epAppRoleFunction20,
            epAppRoleFunction21, epAppRoleFunction22, epAppRoleFunction23, epAppRoleFunction24, epAppRoleFunction25,
            epAppRoleFunction26, epAppRoleFunction27, epAppRoleFunction28));

    epAppRoleFunctionService.saveAll(epAppRoleFunctions);

    EpBasicAuthAccount epBasicAuthAccount = EpBasicAuthAccount.builder().extAppName("JIRA").username("jira").password("6APqvG4AU2rfLgCvMdySwQ==").activeYn(true).build();
    epBasicAuthAccountService.save(epBasicAuthAccount);

    // EP_MICROSERVICE TABLE

    EpMicroservice epMicroservice1 = EpMicroservice.builder().name("News Microservice").description("News").appId(app).endpointUrl("http://portal-app:8989/ONAPPORTAL/commonWidgets").securityType("Basic Authentication").username("portal").password("6APqvG4AU2rfLgCvMdySwQ==").active(true).build();
    EpMicroservice epMicroservice2 = EpMicroservice.builder().name("Events Microservice").description("Events").appId(app).endpointUrl("http://portal-app:8989/ONAPPORTAL/commonWidgets").securityType("Basic Authentication").username("portal").password("APqvG4AU2rfLgCvMdySwQ==").active(true).build();
    EpMicroservice epMicroservice3 = EpMicroservice.builder().name("Resources Microservice").description("Resources").appId(app).endpointUrl("http://portal-app:8989/ONAPPORTAL/commonWidgets").securityType("Basic Authentication").username("portal").password("APqvG4AU2rfLgCvMdySwQ==").active(true).build();
    EpMicroservice epMicroservice4 = EpMicroservice.builder().name("Portal-Common-Scheduler Microservice").description("Portal-Common-Scheduler").appId(app).endpointUrl("http://portal-app:8989/ONAPPORTAL/commonWidgets").securityType("Basic Authentication").username("portal").password("APqvG4AU2rfLgCvMdySwQ==").active(true).build();

    List<EpMicroservice> epMicroservices = new ArrayList<>(Arrays.asList(epMicroservice1, epMicroservice2, epMicroservice3, epMicroservice4));

    epMicroserviceService.saveAll(epMicroservices);

    // ep_microservice_parameter table

    EpMicroserviceParameter parameter1 = EpMicroserviceParameter.builder().serviceId(epMicroservice1).paraKey("resourceType").paraValue("NEWS").build();
    EpMicroserviceParameter parameter2 = EpMicroserviceParameter.builder().serviceId(epMicroservice2).paraKey("resourceType").paraValue("EVENTS").build();
    EpMicroserviceParameter parameter3 = EpMicroserviceParameter.builder().serviceId(epMicroservice3).paraKey("resourceType").paraValue("IMPORTANTRESOURCES").build();
    EpMicroserviceParameter parameter4 = EpMicroserviceParameter.builder().serviceId(epMicroservice4).paraKey("resourceType").paraValue(null).build();

    List<EpMicroserviceParameter> epMicroserviceParameters = new ArrayList<>(Arrays.asList(parameter1, parameter2, parameter3, parameter4));

    epMicroserviceParameterService.saveAll(epMicroserviceParameters);

    // ep_widget_catalog table
    //TODO should we connect serviceId to EpMicroservice?

    EpWidgetCatalog epWidgetCatalog1 = EpWidgetCatalog.builder().wdgName("News").serviceId(1L).wdgDesc("News").wdgFileLoc("news-widget.zip").allUserFlag(true).build();
    EpWidgetCatalog epWidgetCatalog2 = EpWidgetCatalog.builder().wdgName("Events").serviceId(2L).wdgDesc("Events").wdgFileLoc("events-widget.zip").allUserFlag(true).build();
    EpWidgetCatalog epWidgetCatalog3 = EpWidgetCatalog.builder().wdgName("Resources").serviceId(3L).wdgDesc("Resources").wdgFileLoc("resources-widget.zip").allUserFlag(true).build();
    EpWidgetCatalog epWidgetCatalog4 = EpWidgetCatalog.builder().wdgName("Portal-Common-Scheduler").serviceId(4L).wdgDesc("Portal-Common-Scheduler").wdgFileLoc("portal-common-scheduler-widget.zip").allUserFlag(true).build();

    List<EpWidgetCatalog> epWidgetCatalogs = new ArrayList<>(Arrays.asList(epWidgetCatalog1, epWidgetCatalog2, epWidgetCatalog3, epWidgetCatalog4));

    epWidgetCatalogService.saveAll(epWidgetCatalogs);

    // ep_widget_catalog_files table
    //TODO

    //EpWidgetCatalogFiles epWidgetCatalogFiles = EpWidgetCatalogFiles.builder().fileId().widgetId().widgetName().frameworkJs().controllerJs().markupHtml().widgetCss().build();

    // fn_app_contact_us table

    FnAppContactUs fnAppContactUs1 = FnAppContactUs.builder().fnApp(app2).contactName("Portal SDK Team").contactEmail("portal@lists.onap.org").url("https://wiki.onap.org/display/DW/Approved+Projects").activeYn(null).description("xDemo Application").build();
    FnAppContactUs fnAppContactUs2 = FnAppContactUs.builder().fnApp(app3).contactName("DBC Team").contactEmail("portal@lists.onap.org").url("https://wiki.onap.org/display/DW/Approved+Projects").activeYn(null).description("DBC").build();
    FnAppContactUs fnAppContactUs3 = FnAppContactUs.builder().fnApp(app4).contactName("SDC Team").contactEmail("sdc@lists.onap.org").url("https://wiki.onap.org/display/DW/Approved+Projects").activeYn(null).description("Service Design and Creation (SDC).").build();
    FnAppContactUs fnAppContactUs4 = FnAppContactUs.builder().fnApp(app5).contactName("Policy Team").contactEmail("policy@lists.onap.org").url("https://wiki.onap.org/display/DW/Approved+Projects").activeYn(null).description("Policy").build();
    FnAppContactUs fnAppContactUs5 = FnAppContactUs.builder().fnApp(app6).contactName("VID Team").contactEmail("vid@lists.onap.org").url("https://wiki.onap.org/display/DW/Approved+Projects").activeYn(null).description("Virtual Infrastructure Design.").build();
    FnAppContactUs fnAppContactUs6 = FnAppContactUs.builder().fnApp(app7).contactName("AAI UI Team").contactEmail("aaiui@lists.onap.org").url("https://wiki.onap.org/display/DW/Approved+Projects").activeYn(null).description("AAI UI Application").build();
    FnAppContactUs fnAppContactUs7 = FnAppContactUs.builder().fnApp(app8).contactName("CLI Team").contactEmail("onap-discuss@lists.onap.org").url("https://wiki.onap.org/display/DW/Approved+Projects").activeYn(null).description("CLI Application").build();
    FnAppContactUs fnAppContactUs8 = FnAppContactUs.builder().fnApp(app10).contactName("SO Team").contactEmail("so@lists.onap.org").url("https://wiki.onap.org/display/DW/Approved+Projects").activeYn(null).description("Service Orchestration (SO).").build();

    List<FnAppContactUs> fnAppContactUses = new ArrayList<>(Arrays.asList(fnAppContactUs1, fnAppContactUs2, fnAppContactUs3, fnAppContactUs4, fnAppContactUs5, fnAppContactUs6, fnAppContactUs7, fnAppContactUs8));

    fnAppContactUsService.saveAll(fnAppContactUses);

    // fn_common_widget_data table

    FnCommonWidgetData fnCommonWidgetData1  = FnCommonWidgetData.builder().category("NEWS").href("https://www.onap.org/announcement/2017/09/27/open-network-automation-platform-onap-project-continues-rapid-membership-growth").title("Open Network Automation Platform ONAP Project Continues Rapid Membership Growth").content(null).eventDate(null).sortOrder(10L).build();
    FnCommonWidgetData fnCommonWidgetData2  = FnCommonWidgetData.builder().category("NEWS").href("https://www.onap.org/announcement/2017/02/23/the-linux-foundation-announces-the-formation-of-a-new-project-to-help-accelerate-innovation-in-open-networking-automation").title("The Linux Foundation Announces Merger of Open Source ECOMP and OPEN-O to Form New Open Network Automation Platform ONAP Project").content(null).eventDate(null).sortOrder(20L).build();
    FnCommonWidgetData fnCommonWidgetData3  = FnCommonWidgetData.builder().category("NEWS").href("http://about.att.com/story/orange_testing_att_open_source_ecomp_platform.html").title("Orange Testing AT&Ts Open Source ECOMP Platform for Building Software-Defined Network Capabilities").content(null).eventDate(null).sortOrder(30L).build();
    FnCommonWidgetData fnCommonWidgetData4  = FnCommonWidgetData.builder().category("NEWS").href("http://about.att.com/innovationblog/linux_foundation").title("Opening up ECOMP: Our Network Operating System for SDN").content(null).eventDate(null).sortOrder(40L).build();
    FnCommonWidgetData fnCommonWidgetData5  = FnCommonWidgetData.builder().category("EVENTS").href("https://onapbeijing2017.sched.com/list/descriptions/").title("ONAP Beijing Release Developer Forum").content(null).eventDate("2017-12-11").sortOrder(1L).build();
    FnCommonWidgetData fnCommonWidgetData6  = FnCommonWidgetData.builder().category("EVENTS").href("https://events.linuxfoundation.org/events/open-networking-summit-north-america-2018").title("Open Networking Summit").content(null).eventDate("2018-03-26").sortOrder(2L).build();
    FnCommonWidgetData fnCommonWidgetData7  = FnCommonWidgetData.builder().category("IMPORTANTRESOURCES").href("http://onap.readthedocs.io/en/latest/guides/onap-developer/developing/index.html").title("Development Guides").content(null).eventDate(null).sortOrder(1L).build();
    FnCommonWidgetData fnCommonWidgetData8  = FnCommonWidgetData.builder().category("IMPORTANTRESOURCES").href("https://wiki.onap.org/").title("ONAP Wiki").content(null).eventDate(null).sortOrder(2L).build();
    FnCommonWidgetData fnCommonWidgetData9  = FnCommonWidgetData.builder().category("IMPORTANTRESOURCES").href("http://onap.readthedocs.io/en/latest/guides/onap-developer/developing/index.html#portal-platform").title("ONAP Portal Documentation").content(null).eventDate(null).sortOrder(3L).build();
    FnCommonWidgetData fnCommonWidgetData10  = FnCommonWidgetData.builder().category("IMPORTANTRESOURCES").href("http://onap.readthedocs.io/en/latest/guides/onap-developer/architecture/index.html#architecture").title("ONAP Architecture").content(null).eventDate(null).sortOrder(4L).build();

    List<FnCommonWidgetData> fnCommonWidgetDataList = new ArrayList<>(Arrays.asList(fnCommonWidgetData1, fnCommonWidgetData2, fnCommonWidgetData3, fnCommonWidgetData4, fnCommonWidgetData5, fnCommonWidgetData6, fnCommonWidgetData7, fnCommonWidgetData8, fnCommonWidgetData9, fnCommonWidgetData10));

    fnCommonWidgetDataService.saveAll(fnCommonWidgetDataList);

    // fn_language table
    //TODO fix china text
    FnLanguage language1 = new FnLanguage("English", "EN");
    FnLanguage language2 = new FnLanguage("ç®€ä½“ä¸\u00ADæ–‡", "CN");

    fnLanguageService.save(language1);
    fnLanguageService.save(language2);

    // fn_display_text table
    // TODO should we change languageId from long type to FnLanguage?

    FnDisplayText fnDisplayText1 = FnDisplayText.builder().languageId(1L).textId(2L).textLabel("Home").build();
    FnDisplayText fnDisplayText2 = FnDisplayText.builder().languageId(1L).textId(3L).textLabel("Application Catalog").build();
    FnDisplayText fnDisplayText3 = FnDisplayText.builder().languageId(1L).textId(4L).textLabel("Widget Catalog").build();
    FnDisplayText fnDisplayText4 = FnDisplayText.builder().languageId(1L).textId(5L).textLabel("Admins").build();
    FnDisplayText fnDisplayText5 = FnDisplayText.builder().languageId(1L).textId(6L).textLabel("Roles").build();
    FnDisplayText fnDisplayText6 = FnDisplayText.builder().languageId(1L).textId(7L).textLabel("Users").build();
    FnDisplayText fnDisplayText7 = FnDisplayText.builder().languageId(1L).textId(8L).textLabel("Portal Admins").build();
    FnDisplayText fnDisplayText8 = FnDisplayText.builder().languageId(1L).textId(9L).textLabel("Application Onboarding").build();
    FnDisplayText fnDisplayText9 = FnDisplayText.builder().languageId(1L).textId(10L).textLabel("Widget Onboarding").build();
    FnDisplayText fnDisplayText10 = FnDisplayText.builder().languageId(1L).textId(11L).textLabel("Edit Functional Menu").build();
    FnDisplayText fnDisplayText11 = FnDisplayText.builder().languageId(1L).textId(12L).textLabel("User Notifications").build();
    FnDisplayText fnDisplayText12 = FnDisplayText.builder().languageId(1L).textId(13L).textLabel("Microservice Onboarding").build();
    FnDisplayText fnDisplayText13 = FnDisplayText.builder().languageId(1L).textId(15L).textLabel("App Account Management").build();
    FnDisplayText fnDisplayText14 = FnDisplayText.builder().languageId(2L).textId(2L).textLabel("ä¸»é¡µ").build();
    FnDisplayText fnDisplayText15 = FnDisplayText.builder().languageId(2L).textId(3L).textLabel("åº”ç”¨ç›®å½•").build();
    FnDisplayText fnDisplayText16 = FnDisplayText.builder().languageId(2L).textId(4L).textLabel("éƒ¨ä»¶ç›®å½•").build();
    FnDisplayText fnDisplayText17 = FnDisplayText.builder().languageId(2L).textId(5L).textLabel("ç®¡ç†å‘˜").build();
    FnDisplayText fnDisplayText18 = FnDisplayText.builder().languageId(2L).textId(6L).textLabel("è§’è‰²").build();
    FnDisplayText fnDisplayText19 = FnDisplayText.builder().languageId(2L).textId(7L).textLabel("ç”¨æˆ·").build();
    FnDisplayText fnDisplayText20 = FnDisplayText.builder().languageId(2L).textId(8L).textLabel("é—¨æˆ·ç®¡ç†å‘˜").build();
    FnDisplayText fnDisplayText21 = FnDisplayText.builder().languageId(2L).textId(9L).textLabel("åº”ç”¨ç®¡ç†").build();
    FnDisplayText fnDisplayText22 = FnDisplayText.builder().languageId(2L).textId(10L).textLabel("éƒ¨ä»¶ç®¡ç†").build();
    FnDisplayText fnDisplayText23 = FnDisplayText.builder().languageId(2L).textId(11L).textLabel("ç¼–è¾‘åŠŸèƒ½èœå•").build();
    FnDisplayText fnDisplayText24 = FnDisplayText.builder().languageId(2L).textId(12L).textLabel("ç”¨æˆ·é€šçŸ¥").build();
    FnDisplayText fnDisplayText25 = FnDisplayText.builder().languageId(2L).textId(13L).textLabel("å¾®æœåŠ¡ç®¡ç†").build();
    FnDisplayText fnDisplayText26 = FnDisplayText.builder().languageId(2L).textId(15L).textLabel("åº”ç”¨è´¦æˆ·ç®¡ç†").build();

    List<FnDisplayText> fnDisplayTexts = new ArrayList<>(Arrays.asList(fnDisplayText1, fnDisplayText2, fnDisplayText3,
        fnDisplayText4, fnDisplayText5, fnDisplayText6, fnDisplayText7, fnDisplayText8, fnDisplayText9, fnDisplayText10,
        fnDisplayText11, fnDisplayText12, fnDisplayText13, fnDisplayText14, fnDisplayText15, fnDisplayText16, fnDisplayText17,
        fnDisplayText18, fnDisplayText19, fnDisplayText20, fnDisplayText21, fnDisplayText22, fnDisplayText23, fnDisplayText24,
        fnDisplayText25, fnDisplayText26));

    fnDisplayTextService.saveAll(fnDisplayTexts);

    // fn_function table

    FnFunction editNotification = FnFunction.builder().functionCd("edit_notification").functionName("User Notification").build();
    FnFunction getAdminNotifications = FnFunction.builder().functionCd("getAdminNotifications").functionName("Admin Notifications").build();
    FnFunction login = FnFunction.builder().functionCd("login").functionName("Login").build();
    FnFunction menuAdmin = FnFunction.builder().functionCd("menu_admin").functionName("Admin Menu").build();
    FnFunction menuAjax = FnFunction.builder().functionCd("menu_ajax").functionName("Ajax Menu").build();
    FnFunction menuCustomer = FnFunction.builder().functionCd("menu_customer").functionName("Customer Menu").build();
    FnFunction menuCustomerCreate = FnFunction.builder().functionCd("menu_customer_create").functionName("Customer Create").build();
    FnFunction menuFeedback = FnFunction.builder().functionCd("menu_feedback").functionName("Feedback Menu").build();
    FnFunction menuHelp = FnFunction.builder().functionCd("menu_help").functionName("Help Menu").build();
    FnFunction menuHome = FnFunction.builder().functionCd("menu_home").functionName("Home Menu").build();
    FnFunction menuJob = FnFunction.builder().functionCd("menu_job").functionName("Job Menu").build();
    FnFunction menuJobCreate = FnFunction.builder().functionCd("menu_job_create").functionName("Job Create").build();
    FnFunction menuJobDesigner = FnFunction.builder().functionCd("menu_job_designer").functionName("Process in Designer view").build();
    FnFunction menuLogout = FnFunction.builder().functionCd("menu_logout").functionName("Logout Menu").build();
    FnFunction menuMap = FnFunction.builder().functionCd("menu_map").functionName("Map Menu").build();
    FnFunction menuNotes = FnFunction.builder().functionCd("menu_notes").functionName("Notes Menu").build();
    FnFunction menuProcess = FnFunction.builder().functionCd("menu_process").functionName("Process List").build();
    FnFunction menuProfile = FnFunction.builder().functionCd("menu_profile").functionName("Profile Menu").build();
    FnFunction menuProfileCreate = FnFunction.builder().functionCd("menu_profile_create").functionName("Profile Create").build();
    FnFunction menuProfileImport = FnFunction.builder().functionCd("menu_profile_import").functionName("Profile Import").build();
    FnFunction menuReports = FnFunction.builder().functionCd("menu_reports").functionName("Reports Menu").build();
    FnFunction menuSample = FnFunction.builder().functionCd("menu_sample").functionName("Sample Pages Menu").build();
    FnFunction menuTab = FnFunction.builder().functionCd("menu_tab").functionName("Sample Tab Menu").build();
    FnFunction menuTask = FnFunction.builder().functionCd("menu_task").functionName("Task Menu").build();
    FnFunction menuTaskSearch = FnFunction.builder().functionCd("menu_task_search").functionName("Task Search").build();
    FnFunction menuWebAnalytics = FnFunction.builder().functionCd("menu_web_analytics").functionName("Web Analytics").build();
    FnFunction saveNotification = FnFunction.builder().functionCd("saveNotification").functionName("publish notifications").build();
    FnFunction viewReports = FnFunction.builder().functionCd("view_reports").functionName("View Raptor reports").build();

    List<FnFunction> fnFunctions = new ArrayList<>(Arrays.asList(editNotification, getAdminNotifications, login, menuAdmin,
        menuAjax, menuCustomer, menuCustomerCreate, menuFeedback, menuHelp, menuHome, menuJob, menuJobCreate,
        menuJobDesigner, menuLogout, menuMap, menuNotes, menuProcess, menuProfile, menuProfileCreate, menuProfileImport,
        menuReports, menuSample, menuTab, menuTask, menuTaskSearch, menuWebAnalytics, saveNotification, viewReports));

    fnFunctionService.saveAll(fnFunctions);

    // fn_lu_activity table

    FnLuActivity activity1 = FnLuActivity.builder().activity_cd("add_child_role").activity("add_child_role").build();
    FnLuActivity activity2 = FnLuActivity.builder().activity_cd("add_role").activity("add_role").build();
    FnLuActivity activity3 = FnLuActivity.builder().activity_cd("add_role_function").activity("add_role_function").build();
    FnLuActivity activity4 = FnLuActivity.builder().activity_cd("add_user_role").activity("add_user_role").build();
    FnLuActivity activity5 = FnLuActivity.builder().activity_cd("apa").activity("Add Portal Admin").build();
    FnLuActivity appAccess = FnLuActivity.builder().activity_cd("app_access").activity("App Access").build();
    FnLuActivity activity7 = FnLuActivity.builder().activity_cd("dpa").activity("Delete Portal Admin").build();
    FnLuActivity activity8 = FnLuActivity.builder().activity_cd("eaaf").activity("External auth add function").build();
    FnLuActivity activity9 = FnLuActivity.builder().activity_cd("eaar").activity("External auth add role").build();
    FnLuActivity activity10 = FnLuActivity.builder().activity_cd("eadf").activity("External auth delete function").build();
    FnLuActivity activity11 = FnLuActivity.builder().activity_cd("eadr").activity("External auth delete role").build();
    FnLuActivity activity12 = FnLuActivity.builder().activity_cd("eauf").activity("External auth update function").build();
    FnLuActivity activity13 = FnLuActivity.builder().activity_cd("eaurf").activity("External auth update role and function").build();
    FnLuActivity activity14 = FnLuActivity.builder().activity_cd("functional_access").activity("Functional Access").build();
    FnLuActivity activity15 = FnLuActivity.builder().activity_cd("guest_login").activity("Guest Login").build();
    FnLuActivity activity16 = FnLuActivity.builder().activity_cd("left_menu_access").activity("Left Menu Access").build();
    FnLuActivity activity17 = FnLuActivity.builder().activity_cd("login").activity("Login").build();
    FnLuActivity activity18 = FnLuActivity.builder().activity_cd("logout").activity("Logout").build();
    FnLuActivity activity19 = FnLuActivity.builder().activity_cd("mobile_login").activity("Mobile Login").build();
    FnLuActivity activity20 = FnLuActivity.builder().activity_cd("mobile_logout").activity("Mobile Logout").build();
    FnLuActivity activity21 = FnLuActivity.builder().activity_cd("remove_child_role").activity("remove_child_role").build();
    FnLuActivity activity22 = FnLuActivity.builder().activity_cd("remove_role").activity("remove_role").build();
    FnLuActivity activity23 = FnLuActivity.builder().activity_cd("remove_role_function").activity("remove_role_function").build();
    FnLuActivity activity24 = FnLuActivity.builder().activity_cd("remove_user_role").activity("remove_user_role").build();
    FnLuActivity activity25 = FnLuActivity.builder().activity_cd("search").activity("Search").build();
    FnLuActivity tabAccess = FnLuActivity.builder().activity_cd("tab_access").activity("Tab Access").build();
    FnLuActivity activity27 = FnLuActivity.builder().activity_cd("uaa").activity("Update Account Admin").build();
    FnLuActivity activity28 = FnLuActivity.builder().activity_cd("uu").activity("Update User").build();

    List<FnLuActivity> luActivities = new ArrayList<>(Arrays.asList(activity1, activity2, activity3, activity4, activity5,
        appAccess, activity7, activity8, activity9, activity10, activity11, activity12, activity13, activity14, activity15,
        activity16, activity17, activity18, activity19, activity20, activity21, activity22, activity23, activity24, activity25,
        tabAccess, activity27, activity28));

    fnLuActivityService.saveAll(luActivities);

    //fn_lu_alert_method table

    FnLuAlertMethod alertMethod1 = FnLuAlertMethod.builder().alertMethodCd("EMAIL").alertMethod("Email").build();
    FnLuAlertMethod alertMethod2 = FnLuAlertMethod.builder().alertMethodCd("FAX").alertMethod("Fax").build();
    FnLuAlertMethod alertMethod3 = FnLuAlertMethod.builder().alertMethodCd("PAGER").alertMethod("Pager").build();
    FnLuAlertMethod alertMethod4 = FnLuAlertMethod.builder().alertMethodCd("PHONE").alertMethod("Phone").build();
    FnLuAlertMethod alertMethod5 = FnLuAlertMethod.builder().alertMethodCd("SMS").alertMethod("SMS").build();

    List<FnLuAlertMethod> alertMethods = new ArrayList<>(Arrays.asList(alertMethod1, alertMethod2, alertMethod3, alertMethod4, alertMethod5));

    fnLuAlertMethodService.saveAll(alertMethods);

    // fn_lu_menu_set table

    FnLuMenuSet menuSet = FnLuMenuSet.builder().menuSetCd("APP").menuSetName("Application Menu").build();

    fnLuMenuSetService.save(menuSet);

    // fn_lu_tab_set table

    FnLuTabSet fnLuTabSet = FnLuTabSet.builder().tabSetCd("APP").tabSetName("Application Tabs").build();

    fnLuTabSetService.save(fnLuTabSet);

    // fn_lu_priority table

    FnLuPriority low = FnLuPriority.builder().priority("Low").activeYn(true).sortOrder(10L).build();
    FnLuPriority normal = FnLuPriority.builder().priority("Normal").activeYn(true).sortOrder(20L).build();
    FnLuPriority high = FnLuPriority.builder().priority("High").activeYn(true).sortOrder(30L).build();
    FnLuPriority urgent = FnLuPriority.builder().priority("Urgent").activeYn(true).sortOrder(40L).build();
    FnLuPriority fatal = FnLuPriority.builder().priority("Fatal").activeYn(true).sortOrder(50L).build();

    List<FnLuPriority> priorities = new ArrayList<>(Arrays.asList(low, normal, high, urgent, fatal));

    fnLuPriorityService.saveAll(priorities);

    // fn_lu_timezone table

    FnLuTimezone USEastern = FnLuTimezone.builder().timezoneName("US/Eastern").timezoneValue("US/Eastern").build();
    FnLuTimezone USCentral = FnLuTimezone.builder().timezoneName("US/Central").timezoneValue("US/Central").build();
    FnLuTimezone USMountain = FnLuTimezone.builder().timezoneName("US/Mountain").timezoneValue("US/Mountain").build();
    FnLuTimezone USArizona = FnLuTimezone.builder().timezoneName("US/Arizona").timezoneValue("America/Phoenix").build();
    FnLuTimezone USPacific = FnLuTimezone.builder().timezoneName("US/Pacific").timezoneValue("US/Pacific").build();
    FnLuTimezone USAlaska = FnLuTimezone.builder().timezoneName("US/Alaska").timezoneValue("US/Alaska").build();
    FnLuTimezone USHawaii = FnLuTimezone.builder().timezoneName("US/Hawaii").timezoneValue("US/Hawaii").build();

    List<FnLuTimezone> timezones = new ArrayList<>(Arrays.asList(USEastern, USCentral, USMountain, USArizona, USPacific, USAlaska, USHawaii));

    fnLuTimezoneService.saveAll(timezones);

    // fn_menu table

    FnMenu menu1 = FnMenu.builder().label("root").parentId(null).sortOrder(10).action(null).functionCd("menu_home").activeYn(false).menuSetCd(menuSet).separatorYn(false).imageSrc(null).build();
    FnMenu menu2 = FnMenu.builder().label("Home").parentId(menu1).sortOrder(10).action("root.applicationsHome").functionCd("menu_home").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-building-home").build();
    FnMenu menu3 = FnMenu.builder().label("Application Catalog").parentId(menu1).sortOrder(15).action("root.appCatalog").functionCd("menu_home").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-retail-gallery").build();
    FnMenu menu4 = FnMenu.builder().label("Widget Catalog").parentId(menu1).sortOrder(20).action("root.widgetCatalog").functionCd("menu_home").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-retail-gallery").build();
    FnMenu menu5 = FnMenu.builder().label("Admins").parentId(menu1).sortOrder(40).action("root.admins").functionCd("menu_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-content-star").build();
    FnMenu menu6 = FnMenu.builder().label("Roles").parentId(menu1).sortOrder(45).action("root.roles").functionCd("menu_acc_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-people-groupcollaboration").build();
    FnMenu menu7 = FnMenu.builder().label("Users").parentId(menu1).sortOrder(50).action("root.users").functionCd("menu_acc_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-people-groupcollaboration").build();
    FnMenu menu8 = FnMenu.builder().label("Portal Admins").parentId(menu1).sortOrder(60).action("root.portalAdmins").functionCd("menu_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-controls-settingsconnectedactivity").build();
    FnMenu menu9 = FnMenu.builder().label("Application Onboarding").parentId(menu1).sortOrder(70).action("root.applications").functionCd("menu_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-content-grid2").build();
    FnMenu menu10 = FnMenu.builder().label("Widget Onboarding").parentId(menu1).sortOrder(80).action("root.widgetOnboarding").functionCd("menu_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-content-grid2").build();
    FnMenu menu11 = FnMenu.builder().label("Edit Functional Menu").parentId(menu1).sortOrder(90).action("root.functionalMenu").functionCd("menu_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-misc-pen").build();
    FnMenu menu12 = FnMenu.builder().label("User Notifications").parentId(menu1).sortOrder(100).action("root.userNotifications").functionCd("edit_notification").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-controls-settingsconnectedactivity").build();
    FnMenu menu13 = FnMenu.builder().label("Microservice Onboarding").parentId(menu1).sortOrder(110).action("root.microserviceOnboarding").functionCd("menu_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-content-grid2").build();
    FnMenu menu14 = FnMenu.builder().label("App Account Management").parentId(menu1).sortOrder(130).action("root.accountOnboarding").functionCd("menu_admin").activeYn(true).menuSetCd(menuSet).separatorYn(false).imageSrc("icon-content-grid2").build();

    List<FnMenu> fnMenus = new ArrayList<>(Arrays.asList(menu1, menu2, menu3, menu4, menu5, menu6, menu7, menu8, menu9, menu10, menu11, menu12, menu13, menu14));

    fnMenuService.saveAll(fnMenus);

    // fn_menu_functional table

    FnMenuFunctional menuFunctional175 = FnMenuFunctional.builder().columnNum(1L).text("Manage").parentMenuId(null).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional178 = FnMenuFunctional.builder().columnNum(2L).text("Support").parentMenuId(null).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional1 = FnMenuFunctional.builder().columnNum(2L).text("Design").parentMenuId(menuFunctional175).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional2 = FnMenuFunctional.builder().columnNum(8L).text("ECOMP Platform Management").parentMenuId(menuFunctional175).url("").activeYn(false).imageSrc(null).build();
    FnMenuFunctional menuFunctional3 = FnMenuFunctional.builder().columnNum(5L).text("Technology Insertion").parentMenuId(menuFunctional175).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional5 = FnMenuFunctional.builder().columnNum(7L).text("Performance Management").parentMenuId(menuFunctional175).url("").activeYn(false).imageSrc(null).build();
    FnMenuFunctional menuFunctional6 = FnMenuFunctional.builder().columnNum(6L).text("Technology Management").parentMenuId(menuFunctional175).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional7 = FnMenuFunctional.builder().columnNum(4L).text("Capacity Planning").parentMenuId(menuFunctional175).url("").activeYn(false).imageSrc(null).build();
    FnMenuFunctional menuFunctional8 = FnMenuFunctional.builder().columnNum(3L).text("Operations Planning").parentMenuId(menuFunctional175).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional11 = FnMenuFunctional.builder().columnNum(1L).text("Product Design").parentMenuId(menuFunctional1).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional12 = FnMenuFunctional.builder().columnNum(2L).text("Resource/Service Design & Onboarding").parentMenuId(menuFunctional1).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional13 = FnMenuFunctional.builder().columnNum(3L).text("Orchestration recipe/Process Design").parentMenuId(menuFunctional1).url("").activeYn(false).imageSrc(null).build();
    FnMenuFunctional menuFunctional14 = FnMenuFunctional.builder().columnNum(4L).text("Service Graph visualizer").parentMenuId(menuFunctional1).url("").activeYn(false).imageSrc(null).build();
    FnMenuFunctional menuFunctional15 = FnMenuFunctional.builder().columnNum(5L).text("Distribution").parentMenuId(menuFunctional1).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional16 = FnMenuFunctional.builder().columnNum(6L).text("Testing").parentMenuId(menuFunctional1).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional17 = FnMenuFunctional.builder().columnNum(7L).text("Simulation").parentMenuId(menuFunctional1).url("").activeYn(false).imageSrc(null).build();
    FnMenuFunctional menuFunctional18 = FnMenuFunctional.builder().columnNum(8L).text("Certification").parentMenuId(menuFunctional1).url("").activeYn(false).imageSrc(null).build();
    FnMenuFunctional menuFunctional19 = FnMenuFunctional.builder().columnNum(9L).text("Policy Creation/Management").parentMenuId(menuFunctional1).url("http://policy.api.simpledemo.onap.org:8443/onap/policy").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional20 = FnMenuFunctional.builder().columnNum(10L).text("Catalog Browser").parentMenuId(menuFunctional1).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional24 = FnMenuFunctional.builder().columnNum(5L).text("Create/Manage Policy").parentMenuId(menuFunctional12).url("http://policy.api.simpledemo.onap.org:8443/onap/policy").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional56 = FnMenuFunctional.builder().columnNum(1L).text("Policy Engineering").parentMenuId(menuFunctional8).url("http://policy.api.simpledemo.onap.org:8443/onap/policy").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional115 = FnMenuFunctional.builder().columnNum(1L).text("Test/Approve a Resource or Service").parentMenuId(menuFunctional16).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/dashboard").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional130 = FnMenuFunctional.builder().columnNum(1L).text("Favorites").parentMenuId(menuFunctional175).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional139 = FnMenuFunctional.builder().columnNum(2L).text("Approve a Service for distribution").parentMenuId(menuFunctional12).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/dashboard").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional142 = FnMenuFunctional.builder().columnNum(3L).text("Create a License model").parentMenuId(menuFunctional12).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/onboardVendor").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional145 = FnMenuFunctional.builder().columnNum(1L).text("Distribute a Service").parentMenuId(menuFunctional15).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/dashboard").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional181 = FnMenuFunctional.builder().columnNum(1L).text("Contact Us").parentMenuId(menuFunctional178).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional184 = FnMenuFunctional.builder().columnNum(2L).text("Get Access").parentMenuId(menuFunctional178).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional301 = FnMenuFunctional.builder().columnNum(1L).text("Create a Product").parentMenuId(menuFunctional11).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/dashboard").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional304 = FnMenuFunctional.builder().columnNum(2L).text("Create a Vendor Software Product").parentMenuId(menuFunctional11).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/onboardVendor").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional307 = FnMenuFunctional.builder().columnNum(1L).text("Manage a Resource/Service").parentMenuId(menuFunctional20).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/catalog").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional310 = FnMenuFunctional.builder().columnNum(2L).text("Manage a Product").parentMenuId(menuFunctional20).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/catalog").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional313 = FnMenuFunctional.builder().columnNum(3L).text("View a Resource/Service/Product").parentMenuId(menuFunctional20).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/catalog").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional316 = FnMenuFunctional.builder().columnNum(11L).text("Administration").parentMenuId(menuFunctional1).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional148 = FnMenuFunctional.builder().columnNum(1L).text("User Management / Category Management").parentMenuId(menuFunctional316).url("http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/adminDashboard").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional317 = FnMenuFunctional.builder().columnNum(1L).text("Message Bus Management").parentMenuId(menuFunctional6).url("http://portal.api.simpledemo.onap.org:8989/ECOMPDBCAPP/dbc#/dmaap").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional318 = FnMenuFunctional.builder().columnNum(1L).text("Infrastructure Provisioning").parentMenuId(menuFunctional3).url("").activeYn(true).imageSrc(null).build();
    FnMenuFunctional menuFunctional319 = FnMenuFunctional.builder().columnNum(1L).text("Infrastructure VNF Provisioning").parentMenuId(menuFunctional318).url("https://vid.api.simpledemo.onap.org:8443/vid/welcome.htm").activeYn(true).imageSrc(null).build();

    List<FnMenuFunctional> menuFunctionals = new ArrayList<>(Arrays.asList(menuFunctional175, menuFunctional178,
        menuFunctional1, menuFunctional2, menuFunctional3, menuFunctional5, menuFunctional6, menuFunctional7,
        menuFunctional8, menuFunctional11, menuFunctional12, menuFunctional13, menuFunctional14, menuFunctional15,
        menuFunctional16, menuFunctional17, menuFunctional18, menuFunctional19, menuFunctional20, menuFunctional24,
        menuFunctional56, menuFunctional115, menuFunctional130, menuFunctional139, menuFunctional142, menuFunctional145,
        menuFunctional181, menuFunctional184, menuFunctional301, menuFunctional304, menuFunctional307, menuFunctional310,
        menuFunctional313, menuFunctional316, menuFunctional148, menuFunctional317, menuFunctional318, menuFunctional319));


    fnMenuFunctionalService.saveAll(menuFunctionals);

    // fn_menu_functional_ancestors table

    FnMenuFunctionalAncestors ancestors1 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional175).ancestorMenuId(menuFunctional175).depth(0).build();
    FnMenuFunctionalAncestors ancestors2 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional178).ancestorMenuId(menuFunctional178).depth(0).build();
    FnMenuFunctionalAncestors ancestors3 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional11).ancestorMenuId(menuFunctional11).depth(0).build();
    FnMenuFunctionalAncestors ancestors4 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional12).ancestorMenuId(menuFunctional12).depth(0).build();
    FnMenuFunctionalAncestors ancestors5 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional13).ancestorMenuId(menuFunctional13).depth(0).build();
    FnMenuFunctionalAncestors ancestors6 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional14).ancestorMenuId(menuFunctional14).depth(0).build();
    FnMenuFunctionalAncestors ancestors7 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional15).ancestorMenuId(menuFunctional15).depth(0).build();
    FnMenuFunctionalAncestors ancestors8 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional16).ancestorMenuId(menuFunctional16).depth(0).build();
    FnMenuFunctionalAncestors ancestors9 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional17).ancestorMenuId(menuFunctional17).depth(0).build();
    FnMenuFunctionalAncestors ancestors10 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional18).ancestorMenuId(menuFunctional18).depth(0).build();
    FnMenuFunctionalAncestors ancestors11 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional19).ancestorMenuId(menuFunctional19).depth(0).build();
    FnMenuFunctionalAncestors ancestors12 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional20).ancestorMenuId(menuFunctional20).depth(0).build();
    FnMenuFunctionalAncestors ancestors13 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional316).ancestorMenuId(menuFunctional316).depth(0).build();
    FnMenuFunctionalAncestors ancestors14 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional318).ancestorMenuId(menuFunctional318).depth(0).build();
    FnMenuFunctionalAncestors ancestors15 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional317).ancestorMenuId(menuFunctional317).depth(0).build();
    FnMenuFunctionalAncestors ancestors16 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional56).ancestorMenuId(menuFunctional56).depth(0).build();
    FnMenuFunctionalAncestors ancestors17 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional301).ancestorMenuId(menuFunctional301).depth(0).build();
    FnMenuFunctionalAncestors ancestors18 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional304).ancestorMenuId(menuFunctional304).depth(0).build();
    FnMenuFunctionalAncestors ancestors19 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional24).ancestorMenuId(menuFunctional24).depth(0).build();
    FnMenuFunctionalAncestors ancestors20 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional139).ancestorMenuId(menuFunctional139).depth(0).build();
    FnMenuFunctionalAncestors ancestors21 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional142).ancestorMenuId(menuFunctional142).depth(0).build();
    FnMenuFunctionalAncestors ancestors22 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional145).ancestorMenuId(menuFunctional145).depth(0).build();
    FnMenuFunctionalAncestors ancestors23 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional115).ancestorMenuId(menuFunctional115).depth(0).build();
    FnMenuFunctionalAncestors ancestors24 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional307).ancestorMenuId(menuFunctional307).depth(0).build();
    FnMenuFunctionalAncestors ancestors25 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional310).ancestorMenuId(menuFunctional310).depth(0).build();
    FnMenuFunctionalAncestors ancestors26 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional313).ancestorMenuId(menuFunctional313).depth(0).build();
    FnMenuFunctionalAncestors ancestors27 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional1).ancestorMenuId(menuFunctional1).depth(0).build();
    FnMenuFunctionalAncestors ancestors28 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional2).ancestorMenuId(menuFunctional2).depth(0).build();
    FnMenuFunctionalAncestors ancestors29 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional3).ancestorMenuId(menuFunctional3).depth(0).build();
    FnMenuFunctionalAncestors ancestors30 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional5).ancestorMenuId(menuFunctional5).depth(0).build();
    FnMenuFunctionalAncestors ancestors31 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional6).ancestorMenuId(menuFunctional6).depth(0).build();
    FnMenuFunctionalAncestors ancestors32 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional7).ancestorMenuId(menuFunctional7).depth(0).build();
    FnMenuFunctionalAncestors ancestors33 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional8).ancestorMenuId(menuFunctional8).depth(0).build();
    FnMenuFunctionalAncestors ancestors34 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional130).ancestorMenuId(menuFunctional130).depth(0).build();
    FnMenuFunctionalAncestors ancestors35 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional181).ancestorMenuId(menuFunctional181).depth(0).build();
    FnMenuFunctionalAncestors ancestors36 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional184).ancestorMenuId(menuFunctional184).depth(0).build();
    FnMenuFunctionalAncestors ancestors37 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional148).ancestorMenuId(menuFunctional148).depth(0).build();
    FnMenuFunctionalAncestors ancestors38 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional319).ancestorMenuId(menuFunctional319).depth(0).build();
    FnMenuFunctionalAncestors ancestors64 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional11).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors65 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional12).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors66 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional13).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors67 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional14).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors68 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional15).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors69 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional16).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors70 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional17).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors71 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional18).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors72 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional19).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors73 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional20).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors74 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional316).ancestorMenuId(menuFunctional1).depth(1).build();
    FnMenuFunctionalAncestors ancestors75 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional318).ancestorMenuId(menuFunctional3).depth(1).build();
    FnMenuFunctionalAncestors ancestors76 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional317).ancestorMenuId(menuFunctional6).depth(1).build();
    FnMenuFunctionalAncestors ancestors77 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional56).ancestorMenuId(menuFunctional8).depth(1).build();
    FnMenuFunctionalAncestors ancestors78 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional301).ancestorMenuId(menuFunctional11).depth(1).build();
    FnMenuFunctionalAncestors ancestors79 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional304).ancestorMenuId(menuFunctional11).depth(1).build();
    FnMenuFunctionalAncestors ancestors80 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional24).ancestorMenuId(menuFunctional12).depth(1).build();
    FnMenuFunctionalAncestors ancestors81 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional139).ancestorMenuId(menuFunctional12).depth(1).build();
    FnMenuFunctionalAncestors ancestors82 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional142).ancestorMenuId(menuFunctional12).depth(1).build();
    FnMenuFunctionalAncestors ancestors83 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional145).ancestorMenuId(menuFunctional15).depth(1).build();
    FnMenuFunctionalAncestors ancestors84 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional115).ancestorMenuId(menuFunctional16).depth(1).build();
    FnMenuFunctionalAncestors ancestors85 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional307).ancestorMenuId(menuFunctional20).depth(1).build();
    FnMenuFunctionalAncestors ancestors86 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional310).ancestorMenuId(menuFunctional20).depth(1).build();
    FnMenuFunctionalAncestors ancestors87 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional313).ancestorMenuId(menuFunctional20).depth(1).build();
    FnMenuFunctionalAncestors ancestors88 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional1).ancestorMenuId(menuFunctional175).depth(1).build();
    FnMenuFunctionalAncestors ancestors89 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional2).ancestorMenuId(menuFunctional175).depth(1).build();
    FnMenuFunctionalAncestors ancestors90 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional3).ancestorMenuId(menuFunctional175).depth(1).build();
    FnMenuFunctionalAncestors ancestors91 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional5).ancestorMenuId(menuFunctional175).depth(1).build();
    FnMenuFunctionalAncestors ancestors92 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional6).ancestorMenuId(menuFunctional175).depth(1).build();
    FnMenuFunctionalAncestors ancestors93 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional7).ancestorMenuId(menuFunctional175).depth(1).build();
    FnMenuFunctionalAncestors ancestors94 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional8).ancestorMenuId(menuFunctional175).depth(1).build();
    FnMenuFunctionalAncestors ancestors95 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional130).ancestorMenuId(menuFunctional175).depth(1).build();
    FnMenuFunctionalAncestors ancestors96 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional181).ancestorMenuId(menuFunctional178).depth(1).build();
    FnMenuFunctionalAncestors ancestors97 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional184).ancestorMenuId(menuFunctional178).depth(1).build();
    FnMenuFunctionalAncestors ancestors98 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional148).ancestorMenuId(menuFunctional316).depth(1).build();
    FnMenuFunctionalAncestors ancestors99 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional319).ancestorMenuId(menuFunctional318).depth(1).build();
    FnMenuFunctionalAncestors ancestors127 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional301).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors128 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional304).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors129 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional24).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors130 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional139).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors131 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional142).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors132 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional145).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors133 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional115).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors134 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional307).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors135 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional310).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors136 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional313).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors137 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional148).ancestorMenuId(menuFunctional1).depth(2).build();
    FnMenuFunctionalAncestors ancestors138 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional319).ancestorMenuId(menuFunctional3).depth(2).build();
    FnMenuFunctionalAncestors ancestors139 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional11).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors140 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional12).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors141 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional13).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors142 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional14).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors143 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional15).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors144 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional16).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors145 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional17).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors146 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional18).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors147 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional19).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors148 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional20).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors149 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional316).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors150 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional318).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors151 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional317).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors152 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional56).ancestorMenuId(menuFunctional175).depth(2).build();
    FnMenuFunctionalAncestors ancestors158 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional301).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors159 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional304).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors160 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional24).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors161 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional139).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors162 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional142).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors163 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional145).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors164 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional115).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors165 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional307).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors166 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional310).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors167 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional313).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors168 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional148).ancestorMenuId(menuFunctional175).depth(3).build();
    FnMenuFunctionalAncestors ancestors169 = FnMenuFunctionalAncestors.builder().menuId(menuFunctional319).ancestorMenuId(menuFunctional175).depth(3).build();

    List<FnMenuFunctionalAncestors> ancestors = new ArrayList<>(Arrays.asList(ancestors1, ancestors2,
        ancestors3, ancestors4, ancestors5, ancestors6, ancestors7, ancestors8, ancestors9, ancestors10,
        ancestors11, ancestors12, ancestors13, ancestors14, ancestors15, ancestors16, ancestors17, ancestors18,
        ancestors19, ancestors20, ancestors21, ancestors22, ancestors23, ancestors24, ancestors25, ancestors26,
        ancestors27, ancestors28, ancestors29, ancestors30, ancestors31, ancestors32, ancestors33,
        ancestors34, ancestors35, ancestors36, ancestors37, ancestors38, ancestors64, ancestors65, ancestors66,
        ancestors67, ancestors68, ancestors69, ancestors70, ancestors71, ancestors72, ancestors73, ancestors74,
        ancestors75, ancestors76, ancestors77, ancestors78, ancestors79, ancestors80, ancestors81, ancestors82,
        ancestors83, ancestors84, ancestors85, ancestors86, ancestors87, ancestors88, ancestors89, ancestors90,
        ancestors91, ancestors92, ancestors93, ancestors94, ancestors95, ancestors96, ancestors97, ancestors98,
        ancestors99, ancestors127, ancestors128, ancestors129, ancestors130, ancestors131, ancestors132, ancestors133,
        ancestors134, ancestors135, ancestors136, ancestors137, ancestors138, ancestors139, ancestors140, ancestors141,
        ancestors142, ancestors143, ancestors144, ancestors145, ancestors146, ancestors147, ancestors148, ancestors149,
        ancestors150, ancestors151, ancestors152, ancestors158, ancestors159, ancestors160, ancestors161, ancestors162,
        ancestors163, ancestors164, ancestors165, ancestors166, ancestors167, ancestors168, ancestors169));

    fnMenuFunctionalAncestorsService.saveAll(ancestors);

    // fn_menu_functional_roles table

    FnMenuFunctionalRoles functionalRoles1 = FnMenuFunctionalRoles.builder().menuId(menuFunctional19).appId(app5).roleId(fnRole1007).build();
    FnMenuFunctionalRoles functionalRoles2 = FnMenuFunctionalRoles.builder().menuId(menuFunctional19).appId(app5).roleId(fnRole1006).build();
    FnMenuFunctionalRoles functionalRoles3 = FnMenuFunctionalRoles.builder().menuId(menuFunctional24).appId(app5).roleId(fnRole1007).build();
    FnMenuFunctionalRoles functionalRoles4 = FnMenuFunctionalRoles.builder().menuId(menuFunctional24).appId(app5).roleId(fnRole1006).build();
    FnMenuFunctionalRoles functionalRoles5 = FnMenuFunctionalRoles.builder().menuId(menuFunctional56).appId(app5).roleId(fnRole1007).build();
    FnMenuFunctionalRoles functionalRoles6 = FnMenuFunctionalRoles.builder().menuId(menuFunctional56).appId(app5).roleId(fnRole1006).build();
    FnMenuFunctionalRoles functionalRoles8 = FnMenuFunctionalRoles.builder().menuId(menuFunctional115).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles9 = FnMenuFunctionalRoles.builder().menuId(menuFunctional115).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles10 = FnMenuFunctionalRoles.builder().menuId(menuFunctional139).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles11 = FnMenuFunctionalRoles.builder().menuId(menuFunctional139).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles12 = FnMenuFunctionalRoles.builder().menuId(menuFunctional142).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles13 = FnMenuFunctionalRoles.builder().menuId(menuFunctional142).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles14 = FnMenuFunctionalRoles.builder().menuId(menuFunctional145).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles15 = FnMenuFunctionalRoles.builder().menuId(menuFunctional145).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles16 = FnMenuFunctionalRoles.builder().menuId(menuFunctional148).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles17 = FnMenuFunctionalRoles.builder().menuId(menuFunctional148).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles18 = FnMenuFunctionalRoles.builder().menuId(menuFunctional301).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles19 = FnMenuFunctionalRoles.builder().menuId(menuFunctional301).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles20 = FnMenuFunctionalRoles.builder().menuId(menuFunctional304).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles21 = FnMenuFunctionalRoles.builder().menuId(menuFunctional304).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles22 = FnMenuFunctionalRoles.builder().menuId(menuFunctional307).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles23 = FnMenuFunctionalRoles.builder().menuId(menuFunctional307).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles24 = FnMenuFunctionalRoles.builder().menuId(menuFunctional310).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles25 = FnMenuFunctionalRoles.builder().menuId(menuFunctional310).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles26 = FnMenuFunctionalRoles.builder().menuId(menuFunctional313).appId(app4).roleId(fnRole1004).build();
    FnMenuFunctionalRoles functionalRoles27 = FnMenuFunctionalRoles.builder().menuId(menuFunctional313).appId(app4).roleId(fnRole1005).build();
    FnMenuFunctionalRoles functionalRoles39 = FnMenuFunctionalRoles.builder().menuId(menuFunctional319).appId(app6).roleId(fnRole1009).build();
    FnMenuFunctionalRoles functionalRoles40 = FnMenuFunctionalRoles.builder().menuId(menuFunctional319).appId(app6).roleId(fnRole1008).build();
    FnMenuFunctionalRoles functionalRoles42 = FnMenuFunctionalRoles.builder().menuId(menuFunctional317).appId(app3).roleId(fnRole1003).build();
    FnMenuFunctionalRoles functionalRoles43 = FnMenuFunctionalRoles.builder().menuId(menuFunctional317).appId(app3).roleId(fnRole1002).build();

    List<FnMenuFunctionalRoles> functionalRoles = new ArrayList<>(Arrays.asList(functionalRoles1, functionalRoles2,
        functionalRoles3, functionalRoles4, functionalRoles5, functionalRoles6, functionalRoles8, functionalRoles9,
        functionalRoles10, functionalRoles11, functionalRoles12, functionalRoles13, functionalRoles14, functionalRoles15,
        functionalRoles16, functionalRoles17, functionalRoles18, functionalRoles19, functionalRoles20, functionalRoles21,
        functionalRoles22, functionalRoles23, functionalRoles24, functionalRoles25, functionalRoles26, functionalRoles27,
        functionalRoles39, functionalRoles40, functionalRoles42, functionalRoles43));

    fnMenuFunctionalRolesService.saveAll(functionalRoles);

    // fn_qz_job_details table

    FnQzJobDetails fnQzJobDetails1 = FnQzJobDetails.builder().schedName("Scheduler_20190808_one").jobName("LogJob")
        .jobGroup("AppGroup").description(null).jobClassName("org.onap.portalapp.scheduler.LogJob").isDurable(false)
        .isNonconcurrent(true).isUpdateData(true).requestsRecovery(false).jobData("��\\0\u0005sr\\0\u0015org.quartz.JobDataMap���迩��\u0002\\0\\0xr\\0&org.quartz.utils.StringKeyDirtyFlagMap�\b����](\u0002\\0\u0001Z\\0\u0013allowsTransientDataxr\\0\u001Dorg.quartz.utils.DirtyFlagMap\u0013�.�(v\\n�\u0002\\0\u0002Z\\0\u0005dirtyL\\0\u0003mapt\\0\u000FLjava/util/Map;xp\u0001sr\\0\u0011java.util.HashMap\u0005\u0007���\u0016`�\u0003\\0\u0002F\\0\\nloadFactorI\\0\tthresholdxp?@\\0\\0\\0\\0\\0\fw\b\\0\\0\\0\u0010\\0\\0\\0\u0001t\\0\u0005unitst\\0\u0005bytesx\\0".getBytes()).build();
    FnQzJobDetails fnQzJobDetails2   = FnQzJobDetails.builder().schedName("Scheduler_20190808_one")
        .jobName("PortalSessionTimeoutFeedJob").jobGroup("AppGroup").description(null)
        .jobClassName("org.onap.portalapp.service.sessionmgt.TimeoutHandler").isDurable(false)
        .isNonconcurrent(true).isUpdateData(true).requestsRecovery(false).jobData("��\\0\u0005sr\\0\u0015org.quartz.JobDataMap���迩��\u0002\\0\\0xr\\0&org.quartz.utils.StringKeyDirtyFlagMap�\b����](\u0002\\0\u0001Z\\0\u0013allowsTransientDataxr\\0\u001Dorg.quartz.utils.DirtyFlagMap\u0013�.�(v\\n�\u0002\\0\u0002Z\\0\u0005dirtyL\\0\u0003mapt\\0\u000FLjava/util/Map;xp\\0sr\\0\u0011java.util.HashMap\u0005\u0007���\u0016`�\u0003\\0\u0002F\\0\\nloadFactorI\\0\tthresholdxp?@\\0\\0\\0\\0\\0\u0010w\b\\0\\0\\0\u0010\\0\\0\\0\\0x\\0".getBytes()).build();

    List<FnQzJobDetails> jobDetails = new ArrayList<>(Arrays.asList(fnQzJobDetails1, fnQzJobDetails2));

    fnQzJobDetailsService.saveAll(jobDetails);

    // fn_qz_locks table

    FnQzLocks fnQzLocks1 = new FnQzLocks("Scheduler_20190808_one", "STATE_ACCESS");
    FnQzLocks fnQzLocks2 = new FnQzLocks("Scheduler_20190808_one", "TRIGGER_ACCESS");

    List<FnQzLocks> locks = new ArrayList<>(Arrays.asList(fnQzLocks1, fnQzLocks2));

    fnQzLocksService.saveAll(locks);

    // fn_qz_scheduler_state table

    FnQzSchedulerState schedulerState = FnQzSchedulerState.builder().schedName("Scheduler_20190808_one")
        .instanceName("portal-portal-app-76c9f7bfb5-s8rhd1565254283688").lastCheckinTime(BigInteger.valueOf(1565691615399L))
        .checkinInterval(BigInteger.valueOf(20000L)).build();

    fnQzSchedulerStateService.save(schedulerState);

    // fn_qz_triggers table
    //TODO
    FnQzTriggers trigger1 = FnQzTriggers.builder().fnQzJobDetails(fnQzJobDetails1).schedName("Scheduler_20190808_one").jobName("LogJob").jobGroup("AppGroup").triggerName("LogTrigger").triggerGroup("AppGroup").description(null).nextFireTime(1565691660000L).prevFireTime(1565691600000L).priority(0).triggerState("WAITING").triggerType("CRON").startTime(1565254275000L).endTime(0L).calendarName(null).misfireInstr(BigInteger.valueOf(0L)).jobData("".getBytes()).build();
    FnQzTriggers trigger2 = FnQzTriggers.builder().fnQzJobDetails(fnQzJobDetails2).schedName("Scheduler_20190808_one").jobName("PortalSessionTimeoutFeedJob").jobGroup("AppGroup").triggerName("PortalSessionTimeoutFeedTrigger").triggerGroup("AppGroup").description(null).nextFireTime(1565691900000L).prevFireTime(1565691600000L).priority(0).triggerState("WAITING").triggerType("CRON").startTime(1565254275000L).endTime(0L).calendarName(null).misfireInstr(BigInteger.valueOf(0L)).jobData("".getBytes()).build();

    List<FnQzTriggers> fnQzTriggers = new ArrayList<>(Arrays.asList(trigger1, trigger2));

    // fnQzTriggersService.saveAll(fnQzTriggers);

    // fn_qz_cron_triggers table
    //TODO

    FnQzCronTriggers cronTrigger1 = FnQzCronTriggers.builder().schedName("Scheduler_20190808_one").triggerName("LogTrigger").triggerGroup("AppGroup").cronExpression("0 * * * * ? *").timeZoneId("GMT").build();
    FnQzCronTriggers cronTrigger2 = FnQzCronTriggers.builder().schedName("Scheduler_20190808_one").triggerName("PortalSessionTimeoutFeedTrigger").triggerGroup("AppGroup").cronExpression("0 0/5 * * * ? *").timeZoneId("GMT").build();

    List<FnQzCronTriggers> cronTriggers = new ArrayList<>(Arrays.asList(cronTrigger1, cronTrigger2));

    //fnQzCronTriggersService.saveAll(cronTriggers);

    // fn_restricted_url table
    //TODO
    FnRestrictedUrl url1 = FnRestrictedUrl.builder().restrictedUrl("async_test.htm").functionCd(menuHome).build();
    FnRestrictedUrl url2 = FnRestrictedUrl.builder().restrictedUrl("attachment.htm").functionCd(menuAdmin).build();
    FnRestrictedUrl url3 = FnRestrictedUrl.builder().restrictedUrl("broadcast.htm").functionCd(menuAdmin).build();
    FnRestrictedUrl url4 = FnRestrictedUrl.builder().restrictedUrl("chatWindow.htm").functionCd(menuHome).build();
    FnRestrictedUrl url5 = FnRestrictedUrl.builder().restrictedUrl("contact_list.htm").functionCd(menuHome).build();
    FnRestrictedUrl url6 = FnRestrictedUrl.builder().restrictedUrl("customer_dynamic_list.htm").functionCd(menuHome).build();
    FnRestrictedUrl url7 = FnRestrictedUrl.builder().restrictedUrl("event.htm").functionCd(menuHome).build();
    FnRestrictedUrl url8 = FnRestrictedUrl.builder().restrictedUrl("event_list.htm").functionCd(menuHome).build();
    FnRestrictedUrl url9 = FnRestrictedUrl.builder().restrictedUrl("file_upload.htm").functionCd(menuAdmin).build();
    FnRestrictedUrl url10 = FnRestrictedUrl.builder().restrictedUrl("gauge.htm").functionCd(menuTab).build();
    FnRestrictedUrl url11 = FnRestrictedUrl.builder().restrictedUrl("gmap_controller.htm").functionCd(menuTab).build();
    FnRestrictedUrl url12 = FnRestrictedUrl.builder().restrictedUrl("gmap_frame.htm").functionCd(menuTab).build();
    FnRestrictedUrl url13 = FnRestrictedUrl.builder().restrictedUrl("jbpm_designer.htm").functionCd(menuJobCreate).build();
    FnRestrictedUrl url14 = FnRestrictedUrl.builder().restrictedUrl("jbpm_drools.htm").functionCd(menuJobCreate).build();
    FnRestrictedUrl url15 = FnRestrictedUrl.builder().restrictedUrl("job.htm").functionCd(menuAdmin).build();
    FnRestrictedUrl url16 = FnRestrictedUrl.builder().restrictedUrl("map.htm").functionCd(menuTab).build();
    FnRestrictedUrl url17 = FnRestrictedUrl.builder().restrictedUrl("map_download.htm").functionCd(menuTab).build();
    FnRestrictedUrl url18 = FnRestrictedUrl.builder().restrictedUrl("map_grid_search.htm").functionCd(menuTab).build();
    FnRestrictedUrl url19 = FnRestrictedUrl.builder().restrictedUrl("mobile_welcome.htm").functionCd(menuHome).build();
    FnRestrictedUrl url20 = FnRestrictedUrl.builder().restrictedUrl("process_job.htm").functionCd(menuJobCreate).build();
    FnRestrictedUrl url21 = FnRestrictedUrl.builder().restrictedUrl("profile.htm").functionCd(menuProfileCreate).build();
    FnRestrictedUrl url22 = FnRestrictedUrl.builder().restrictedUrl("raptor.htm").functionCd(menuReports).build();
    FnRestrictedUrl url23 = FnRestrictedUrl.builder().restrictedUrl("raptor.htm").functionCd(viewReports).build();
    FnRestrictedUrl url24 = FnRestrictedUrl.builder().restrictedUrl("raptor2.htm").functionCd(menuReports).build();
    FnRestrictedUrl url25 = FnRestrictedUrl.builder().restrictedUrl("raptor_blob_extract.htm").functionCd(menuReports).build();
    FnRestrictedUrl url26 = FnRestrictedUrl.builder().restrictedUrl("raptor_blob_extract.htm").functionCd(viewReports).build();
    FnRestrictedUrl url27 = FnRestrictedUrl.builder().restrictedUrl("raptor_email_attachment.htm").functionCd(menuReports).build();
    FnRestrictedUrl url28 = FnRestrictedUrl.builder().restrictedUrl("raptor_search.htm").functionCd(menuReports).build();
    FnRestrictedUrl url29 = FnRestrictedUrl.builder().restrictedUrl("report_list.htm").functionCd(menuReports).build();
    FnRestrictedUrl url30 = FnRestrictedUrl.builder().restrictedUrl("role.htm").functionCd(menuAdmin).build();
    FnRestrictedUrl url31 = FnRestrictedUrl.builder().restrictedUrl("role_function.htm").functionCd(menuAdmin).build();
    FnRestrictedUrl url32 = FnRestrictedUrl.builder().restrictedUrl("sample_animated_map.htm").functionCd(menuTab).build();
    FnRestrictedUrl url33 = FnRestrictedUrl.builder().restrictedUrl("sample_map.htm").functionCd(menuHome).build();
    FnRestrictedUrl url34 = FnRestrictedUrl.builder().restrictedUrl("sample_map_2.htm").functionCd(menuTab).build();
    FnRestrictedUrl url35 = FnRestrictedUrl.builder().restrictedUrl("sample_map_3.htm").functionCd(menuTab).build();
    FnRestrictedUrl url36 = FnRestrictedUrl.builder().restrictedUrl("tab2_sub1.htm").functionCd(menuTab).build();
    FnRestrictedUrl url37 = FnRestrictedUrl.builder().restrictedUrl("tab2_sub2_link1.htm").functionCd(menuTab).build();
    FnRestrictedUrl url38 = FnRestrictedUrl.builder().restrictedUrl("tab2_sub2_link2.htm").functionCd(menuTab).build();
    FnRestrictedUrl url39 = FnRestrictedUrl.builder().restrictedUrl("tab2_sub3.htm").functionCd(menuTab).build();
    FnRestrictedUrl url40 = FnRestrictedUrl.builder().restrictedUrl("tab3.htm").functionCd(menuTab).build();
    FnRestrictedUrl url41 = FnRestrictedUrl.builder().restrictedUrl("tab4.htm").functionCd(menuTab).build();
    FnRestrictedUrl url42 = FnRestrictedUrl.builder().restrictedUrl("template.jsp").functionCd(menuHome).build();
    FnRestrictedUrl url43 = FnRestrictedUrl.builder().restrictedUrl("test.htm").functionCd(menuAdmin).build();

    List<FnRestrictedUrl> urls = new ArrayList<>(Arrays.asList(url1, url2, url3, url4, url5, url6, url7, url8, url9,
        url10, url11, url12, url13, url14, url15, url16, url17, url18, url19, url20, url21, url22, url23, url24, url25,
        url26, url27, url28, url29, url30, url31, url32, url33, url34, url35, url36, url37, url38, url39, url40, url41,
        url42, url43));

    //fnRestrictedUrlService.saveAll(urls);

    // fn_role_composite table
    //TODO
    FnRoleComposite roleComposite = FnRoleComposite.builder().parentRoles(fnRole1).childRoles(fnRole16).build();

    //fnRoleCompositeService.save(roleComposite);

    // fn_role_function table

    FnRoleFunction roleFunction1 = FnRoleFunction.builder().role(fnRole1).functionCd(login).build();
    FnRoleFunction roleFunction2 = FnRoleFunction.builder().role(fnRole1).functionCd(menuAdmin).build();
    FnRoleFunction roleFunction3 = FnRoleFunction.builder().role(fnRole1).functionCd(menuAjax).build();
    FnRoleFunction roleFunction4 = FnRoleFunction.builder().role(fnRole1).functionCd(menuCustomer).build();
    FnRoleFunction roleFunction5 = FnRoleFunction.builder().role(fnRole1).functionCd(menuCustomerCreate).build();
    FnRoleFunction roleFunction6 = FnRoleFunction.builder().role(fnRole1).functionCd(menuFeedback).build();
    FnRoleFunction roleFunction7 = FnRoleFunction.builder().role(fnRole1).functionCd(menuHelp).build();
    FnRoleFunction roleFunction8 = FnRoleFunction.builder().role(fnRole1).functionCd(menuHome).build();
    FnRoleFunction roleFunction9 = FnRoleFunction.builder().role(fnRole1).functionCd(menuJob).build();
    FnRoleFunction roleFunction10 = FnRoleFunction.builder().role(fnRole1).functionCd(menuJobCreate).build();
    FnRoleFunction roleFunction11 = FnRoleFunction.builder().role(fnRole1).functionCd(menuLogout).build();
    FnRoleFunction roleFunction12 = FnRoleFunction.builder().role(fnRole1).functionCd(menuNotes).build();
    FnRoleFunction roleFunction13 = FnRoleFunction.builder().role(fnRole1).functionCd(menuProcess).build();
    FnRoleFunction roleFunction14 = FnRoleFunction.builder().role(fnRole1).functionCd(menuProfile).build();
    FnRoleFunction roleFunction15 = FnRoleFunction.builder().role(fnRole1).functionCd(menuProfileCreate).build();
    FnRoleFunction roleFunction16 = FnRoleFunction.builder().role(fnRole1).functionCd(menuProfileImport).build();
    FnRoleFunction roleFunction17 = FnRoleFunction.builder().role(fnRole1).functionCd(menuReports).build();
    FnRoleFunction roleFunction18 = FnRoleFunction.builder().role(fnRole1).functionCd(menuSample).build();
    FnRoleFunction roleFunction19 = FnRoleFunction.builder().role(fnRole1).functionCd(menuTab).build();
    FnRoleFunction roleFunction20 = FnRoleFunction.builder().role(fnRole16).functionCd(login).build();
    FnRoleFunction roleFunction21 = FnRoleFunction.builder().role(fnRole16).functionCd(menuAjax).build();
    FnRoleFunction roleFunction22 = FnRoleFunction.builder().role(fnRole16).functionCd(menuCustomer).build();
    FnRoleFunction roleFunction23 = FnRoleFunction.builder().role(fnRole16).functionCd(menuCustomerCreate).build();
    FnRoleFunction roleFunction24 = FnRoleFunction.builder().role(fnRole16).functionCd(menuHome).build();
    FnRoleFunction roleFunction25 = FnRoleFunction.builder().role(fnRole16).functionCd(menuLogout).build();
    FnRoleFunction roleFunction26 = FnRoleFunction.builder().role(fnRole16).functionCd(menuMap).build();
    FnRoleFunction roleFunction27 = FnRoleFunction.builder().role(fnRole16).functionCd(menuProfile).build();
    FnRoleFunction roleFunction28 = FnRoleFunction.builder().role(fnRole16).functionCd(menuReports).build();
    FnRoleFunction roleFunction29 = FnRoleFunction.builder().role(fnRole16).functionCd(menuTab).build();
    FnRoleFunction roleFunction30 = FnRoleFunction.builder().role(fnRole950).functionCd(editNotification).build();
    FnRoleFunction roleFunction31 = FnRoleFunction.builder().role(fnRole950).functionCd(getAdminNotifications).build();
    FnRoleFunction roleFunction32 = FnRoleFunction.builder().role(fnRole950).functionCd(saveNotification).build();
    FnRoleFunction roleFunction33 = FnRoleFunction.builder().role(fnRole1010).functionCd(menuWebAnalytics).build();
    FnRoleFunction roleFunction34 = FnRoleFunction.builder().role(fnRole2115).functionCd(menuWebAnalytics).build();

    List<FnRoleFunction> roleFunctions = new ArrayList<>(Arrays.asList(roleFunction1,
        roleFunction2, roleFunction3, roleFunction4, roleFunction5, roleFunction6, roleFunction7, roleFunction8,
        roleFunction9, roleFunction10, roleFunction11, roleFunction12, roleFunction13, roleFunction14, roleFunction15,
        roleFunction16, roleFunction17, roleFunction18, roleFunction19, roleFunction20, roleFunction21, roleFunction22,
        roleFunction23, roleFunction24, roleFunction25, roleFunction26, roleFunction27, roleFunction28, roleFunction29,
        roleFunction30, roleFunction31, roleFunction32, roleFunction33, roleFunction34));

    fnRoleFunctionService.saveAll(roleFunctions);


    // fn_shared_context table

    FnSharedContext sharedContext1 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("b999771d~2d60~4638~a670~d47d17219157").ckey("USER_FIRST_NAME").cvalue("Jimmy").build();
    FnSharedContext sharedContext2 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("b999771d~2d60~4638~a670~d47d17219157").ckey("USER_LAST_NAME").cvalue("Hendrix").build();
    FnSharedContext sharedContext3 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("b999771d~2d60~4638~a670~d47d17219157").ckey("USER_EMAIL").cvalue("admin@onap.org").build();
    FnSharedContext sharedContext4 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("b999771d~2d60~4638~a670~d47d17219157").ckey("USER_ORG_USERID").cvalue("jh0003").build();
    FnSharedContext sharedContext5 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("29cc8f94~5a7d~41f8~b359~432bb903a718").ckey("USER_FIRST_NAME").cvalue("Demo").build();
    FnSharedContext sharedContext6 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("29cc8f94~5a7d~41f8~b359~432bb903a718").ckey("USER_LAST_NAME").cvalue("User").build();
    FnSharedContext sharedContext7 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("29cc8f94~5a7d~41f8~b359~432bb903a718").ckey("USER_EMAIL").cvalue("demo@openecomp.org").build();
    FnSharedContext sharedContext8 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("29cc8f94~5a7d~41f8~b359~432bb903a718").ckey("USER_ORG_USERID").cvalue("demo").build();
    FnSharedContext sharedContext9 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("7e3ced0a~52a3~492a~be53~2885d2df5a43").ckey("USER_FIRST_NAME").cvalue("Demo").build();
    FnSharedContext sharedContext10 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("7e3ced0a~52a3~492a~be53~2885d2df5a43").ckey("USER_LAST_NAME").cvalue("User").build();
    FnSharedContext sharedContext11 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("7e3ced0a~52a3~492a~be53~2885d2df5a43").ckey("USER_EMAIL").cvalue("demo@openecomp.org").build();
    FnSharedContext sharedContext12 = FnSharedContext.builder().created(LocalDateTime.now()).contextId("7e3ced0a~52a3~492a~be53~2885d2df5a43").ckey("USER_ORG_USERID").cvalue("demo").build();

    List<FnSharedContext> sharedContexts = new ArrayList<>(Arrays.asList(sharedContext1, sharedContext2, sharedContext3,
        sharedContext4, sharedContext5, sharedContext6, sharedContext7, sharedContext8, sharedContext9, sharedContext10, sharedContext11, sharedContext12));

    fnSharedContextService.saveAll(sharedContexts);

    // fn_tab table

    FnTab TAB1 = FnTab.builder().tabCd("TAB1").tabName("Tab 1").tabDescr("Tab 1 Information").action("tab1.htm").functionCd(menuTab).activeYn(true).sortDrder(10L).parentTabCd(null).tabSetCd(fnLuTabSet).build();
    FnTab TAB2 = FnTab.builder().tabCd("TAB2").tabName("Tab 2").tabDescr("Tab 2 Information").action("tab2_sub1.htm").functionCd(menuTab).activeYn(true).sortDrder(20L).parentTabCd(null).tabSetCd(fnLuTabSet).build();
    FnTab TAB2_SUB1 = FnTab.builder().tabCd("TAB2_SUB1").tabName("Sub Tab 1").tabDescr("Sub Tab 1 Information").action("tab2_sub1.htm").functionCd(menuTab).activeYn(true).sortDrder(10L).parentTabCd("TAB2").tabSetCd(fnLuTabSet).build();
    FnTab TAB2_SUB1_S1 = FnTab.builder().tabCd("TAB2_SUB1_S1").tabName("Left Tab 1").tabDescr("Sub - Sub Tab 1 Information").action("tab2_sub1.htm").functionCd(menuTab).activeYn(true).sortDrder(10L).parentTabCd("TAB2_SUB1").tabSetCd(fnLuTabSet).build();
    FnTab TAB2_SUB2 = FnTab.builder().tabCd("TAB2_SUB2").tabName("Sub Tab 2").tabDescr("Sub Tab 2 Information").action("tab2_sub2.htm").functionCd(menuTab).activeYn(true).sortDrder(20L).parentTabCd("TAB2").tabSetCd(fnLuTabSet).build();
    FnTab TAB2_SUB3 = FnTab.builder().tabCd("TAB2_SUB3").tabName("Sub Tab 3").tabDescr("Sub Tab 3 Information").action("tab2_sub3.htm").functionCd(menuTab).activeYn(true).sortDrder(30L).parentTabCd("TAB2").tabSetCd(fnLuTabSet).build();
    FnTab TAB3 = FnTab.builder().tabCd("TAB3").tabName("Tab 3").tabDescr("Tab 3 Information").action("tab3.htm").functionCd(menuTab).activeYn(true).sortDrder(30L).parentTabCd(null).tabSetCd(fnLuTabSet).build();
    FnTab TAB4 = FnTab.builder().tabCd("TAB4").tabName("Tab 4").tabDescr("Tab 4 Information").action("tab4.htm").functionCd(menuTab).activeYn(true).sortDrder(40L).parentTabCd(null).tabSetCd(fnLuTabSet).build();

    List<FnTab> fnTabs = new ArrayList<>(Arrays.asList(TAB1, TAB2, TAB2_SUB1, TAB2_SUB1_S1, TAB2_SUB2, TAB2_SUB3, TAB3, TAB4));

    fnTabService.saveAll(fnTabs);

    // fn_tab_selected table

    FnTabSelected tabSelected1 = FnTabSelected.builder().selectedTabCd(TAB1).tab_uri("tab1").build();
    FnTabSelected tabSelected2 = FnTabSelected.builder().selectedTabCd(TAB2).tab_uri("tab2_sub1").build();
    FnTabSelected tabSelected3 = FnTabSelected.builder().selectedTabCd(TAB2).tab_uri("tab2_sub2").build();
    FnTabSelected tabSelected4 = FnTabSelected.builder().selectedTabCd(TAB2).tab_uri("tab2_sub3").build();
    FnTabSelected tabSelected5 = FnTabSelected.builder().selectedTabCd(TAB2_SUB1).tab_uri("tab2_sub1").build();
    FnTabSelected tabSelected6 = FnTabSelected.builder().selectedTabCd(TAB2_SUB1_S1).tab_uri("tab2_sub1").build();
    FnTabSelected tabSelected7 = FnTabSelected.builder().selectedTabCd(TAB2_SUB2).tab_uri("tab2_sub2").build();
    FnTabSelected tabSelected8 = FnTabSelected.builder().selectedTabCd(TAB2_SUB3).tab_uri("tab2_sub3").build();
    FnTabSelected tabSelected9 = FnTabSelected.builder().selectedTabCd(TAB3).tab_uri("tab3").build();
    FnTabSelected tabSelected10 = FnTabSelected.builder().selectedTabCd(TAB4).tab_uri("tab4").build();

    List<FnTabSelected> tabSelecteds = new ArrayList<>(Arrays.asList(tabSelected1, tabSelected2, tabSelected3, tabSelected4,
        tabSelected5, tabSelected6, tabSelected7, tabSelected8, tabSelected9, tabSelected10));

//    fnTabSelectedService.saveAll(tabSelecteds);

    // fn_user table

    FnUser fnUser1 = FnUser.builder().firstName("Demo").lastName("User").email("demo@openecomp.org").orgUserId("demo").loginId("demo").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser2 = FnUser.builder().firstName("Jimmy").lastName("Hendrix").email("admin@onap.org").orgUserId("jh0003").loginId("jh0003").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser3 = FnUser.builder().firstName("Carlos").lastName("Santana").email("designer@onap.org").orgUserId("cs0008").loginId("cs0008").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser4 = FnUser.builder().firstName("Joni").lastName("Mitchell").email("tester@onap.org").orgUserId("jm0007").loginId("jm0007").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser5 = FnUser.builder().firstName("Steve").lastName("Regev").email("ops@onap.org").orgUserId("op0001").loginId("op0001").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser6 = FnUser.builder().firstName("David").lastName("Shadmi").email("governor@onap.org").orgUserId("gv0001").loginId("gv0001").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser7 = FnUser.builder().firstName("Teddy").lastName("Isashar").email("pm1@onap.org").orgUserId("pm0001").loginId("pm0001").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser8 = FnUser.builder().firstName("Eden").lastName("Rozin").email("ps1@onap.org").orgUserId("ps0001").loginId("ps0001").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser9 = FnUser.builder().firstName("vid1").lastName("user").email("vid1@onap.org").orgUserId("vid1").loginId("vid1").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser10 = FnUser.builder().firstName("vid2").lastName("user").email("vid2@onap.org").orgUserId("vid2").loginId("vid2").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser11 = FnUser.builder().firstName("vid3").lastName("user").email("vid3@onap.org").orgUserId("vid3").loginId("vid3").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();
    FnUser fnUser12 = FnUser.builder().firstName("steve").lastName("user").email("steve@onap.org").orgUserId("steve").loginId("steve").loginPwd("demo123").lastLoginDate(LocalDateTime.now()).activeYn(true).createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).isSystemUser(false).isInternalYn(false).guest(false).stateCd("NJ").countryCd("US").timezone(USEastern).languageId(language1).build();

    List<FnUser> fnUsers = new ArrayList<>(Arrays.asList(fnUser1, fnUser2, fnUser3, fnUser4, fnUser5, fnUser6, fnUser7, fnUser8, fnUser9, fnUser10, fnUser11, fnUser12));

    fnUserService.saveAll(fnUsers);

    // ep_pers_user_app_sort table

    EpPersUserAppSort appSort = EpPersUserAppSort.builder().userID(fnUser1).sortPref(0).build();

    epPersUserAppSortService.save(appSort);

    // fn_pers_user_app_sel table

    FnPersUserAppSel appSel1 = FnPersUserAppSel.builder().userId(fnUser1).appId(app7).statusCd("S").build();
    FnPersUserAppSel appSel2 = FnPersUserAppSel.builder().userId(fnUser1).appId(app8).statusCd("S").build();
    FnPersUserAppSel appSel3 = FnPersUserAppSel.builder().userId(fnUser1).appId(app10).statusCd("S").build();
    FnPersUserAppSel appSel4 = FnPersUserAppSel.builder().userId(fnUser1).appId(app5).statusCd("S").build();

    List<FnPersUserAppSel> appSels = new ArrayList<>(Arrays.asList(appSel1, appSel2, appSel3, appSel4));

    fnPersUserAppSelService.saveAll(appSels);

    // fn_audit_log table

    FnAuditLog fnAuditLog1 = FnAuditLog.builder().userId(fnUser1).activityCd(appAccess).auditDate(LocalDateTime.now()).comments("https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html").affectedRecordIdBk(null).affectedRecordId("7").build();
    FnAuditLog fnAuditLog2 = FnAuditLog.builder().userId(fnUser1).activityCd(tabAccess).auditDate(LocalDateTime.now()).comments("https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html?cc=1565259532115").affectedRecordIdBk(null).affectedRecordId("7").build();
    FnAuditLog fnAuditLog3 = FnAuditLog.builder().userId(fnUser1).activityCd(tabAccess).auditDate(LocalDateTime.now()).comments("Home").affectedRecordIdBk(null).affectedRecordId("1").build();
    FnAuditLog fnAuditLog4 = FnAuditLog.builder().userId(fnUser1).activityCd(tabAccess).auditDate(LocalDateTime.now()).comments("https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html?cc=1565259538769").affectedRecordIdBk(null).affectedRecordId("7").build();
    FnAuditLog fnAuditLog5 = FnAuditLog.builder().userId(fnUser1).activityCd(appAccess).auditDate(LocalDateTime.now()).comments("https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html").affectedRecordIdBk(null).affectedRecordId("7").build();
    FnAuditLog fnAuditLog6 = FnAuditLog.builder().userId(fnUser1).activityCd(tabAccess).auditDate(LocalDateTime.now()).comments("https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html?cc=1565259538769").affectedRecordIdBk(null).affectedRecordId("7").build();
    FnAuditLog fnAuditLog7 = FnAuditLog.builder().userId(fnUser1).activityCd(tabAccess).auditDate(LocalDateTime.now()).comments("Home").affectedRecordIdBk(null).affectedRecordId("1").build();

    List<FnAuditLog> auditLogs = new ArrayList<>(Arrays.asList(fnAuditLog1, fnAuditLog2, fnAuditLog3, fnAuditLog4, fnAuditLog5, fnAuditLog6, fnAuditLog7));

    fnAuditLogService.saveAll(auditLogs);

    // fn_user_role table

    FnUserRole userRole1 = FnUserRole.builder().userId(fnUser1).roleId(fnRole1).priority(1L).fnAppId(app).build();
    FnUserRole userRole2 = FnUserRole.builder().userId(fnUser1).roleId(fnRole950).priority(1L).fnAppId(app).build();
    FnUserRole userRole3 = FnUserRole.builder().userId(fnUser1).roleId(fnRole999).priority(1L).fnAppId(app).build();
    FnUserRole userRole4 = FnUserRole.builder().userId(fnUser1).roleId(fnRole999).priority(1L).fnAppId(app2).build();
    FnUserRole userRole5 = FnUserRole.builder().userId(fnUser1).roleId(fnRole999).priority(1L).fnAppId(app3).build();
    FnUserRole userRole6 = FnUserRole.builder().userId(fnUser1).roleId(fnRole999).priority(1L).fnAppId(app4).build();
    FnUserRole userRole7 = FnUserRole.builder().userId(fnUser1).roleId(fnRole999).priority(1L).fnAppId(app5).build();
    FnUserRole userRole8 = FnUserRole.builder().userId(fnUser1).roleId(fnRole999).priority(1L).fnAppId(app6).build();
    FnUserRole userRole9 = FnUserRole.builder().userId(fnUser1).roleId(fnRole999).priority(1L).fnAppId(app7).build();
    FnUserRole userRole10 = FnUserRole.builder().userId(fnUser1).roleId(fnRole1000).priority(1L).fnAppId(app2).build();
    FnUserRole userRole11 = FnUserRole.builder().userId(fnUser1).roleId(fnRole1001).priority(1L).fnAppId(app2).build();
    FnUserRole userRole12 = FnUserRole.builder().userId(fnUser1).roleId(fnRole1002).priority(1L).fnAppId(app3).build();
    FnUserRole userRole13 = FnUserRole.builder().userId(fnUser1).roleId(fnRole1004).priority(1L).fnAppId(app4).build();
    FnUserRole userRole14 = FnUserRole.builder().userId(fnUser1).roleId(fnRole1006).priority(1L).fnAppId(app5).build();
    FnUserRole userRole15 = FnUserRole.builder().userId(fnUser1).roleId(fnRole1008).priority(1L).fnAppId(app6).build();
    FnUserRole userRole16 = FnUserRole.builder().userId(fnUser2).roleId(fnRole999).priority(1L).fnAppId(app4).build();
    FnUserRole userRole17 = FnUserRole.builder().userId(fnUser2).roleId(fnRole1004).priority(1L).fnAppId(app4).build();
    FnUserRole userRole18 = FnUserRole.builder().userId(fnUser3).roleId(fnRole16).priority(null).fnAppId(app4).build();
    FnUserRole userRole19 = FnUserRole.builder().userId(fnUser3).roleId(fnRole1005).priority(null).fnAppId(app4).build();
    FnUserRole userRole20 = FnUserRole.builder().userId(fnUser4).roleId(fnRole16).priority(null).fnAppId(app4).build();
    FnUserRole userRole21 = FnUserRole.builder().userId(fnUser4).roleId(fnRole1005).priority(null).fnAppId(app4).build();
    FnUserRole userRole22 = FnUserRole.builder().userId(fnUser5).roleId(fnRole16).priority(null).fnAppId(app4).build();
    FnUserRole userRole23 = FnUserRole.builder().userId(fnUser5).roleId(fnRole1005).priority(null).fnAppId(app4).build();
    FnUserRole userRole24 = FnUserRole.builder().userId(fnUser6).roleId(fnRole16).priority(null).fnAppId(app4).build();
    FnUserRole userRole25 = FnUserRole.builder().userId(fnUser6).roleId(fnRole1005).priority(null).fnAppId(app4).build();
    FnUserRole userRole26 = FnUserRole.builder().userId(fnUser7).roleId(fnRole16).priority(null).fnAppId(app4).build();
    FnUserRole userRole27 = FnUserRole.builder().userId(fnUser7).roleId(fnRole1005).priority(null).fnAppId(app4).build();
    FnUserRole userRole28 = FnUserRole.builder().userId(fnUser8).roleId(fnRole16).priority(null).fnAppId(app4).build();
    FnUserRole userRole29 = FnUserRole.builder().userId(fnUser8).roleId(fnRole1005).priority(null).fnAppId(app4).build();
    FnUserRole userRole30 = FnUserRole.builder().userId(fnUser9).roleId(fnRole16).priority(null).fnAppId(app6).build();
    FnUserRole userRole31 = FnUserRole.builder().userId(fnUser9).roleId(fnRole999).priority(null).fnAppId(app).build();
    FnUserRole userRole32 = FnUserRole.builder().userId(fnUser9).roleId(fnRole1008).priority(null).fnAppId(app6).build();
    FnUserRole userRole33 = FnUserRole.builder().userId(fnUser10).roleId(fnRole16).priority(null).fnAppId(app6).build();
    FnUserRole userRole34 = FnUserRole.builder().userId(fnUser10).roleId(fnRole1008).priority(null).fnAppId(app6).build();
    FnUserRole userRole35 = FnUserRole.builder().userId(fnUser10).roleId(fnRole1009).priority(null).fnAppId(app6).build();
    FnUserRole userRole36 = FnUserRole.builder().userId(fnUser11).roleId(fnRole16).priority(null).fnAppId(app6).build();
    FnUserRole userRole37 = FnUserRole.builder().userId(fnUser12).roleId(fnRole16).priority(null).fnAppId(app7).build();
    FnUserRole userRole38 = FnUserRole.builder().userId(fnUser12).roleId(fnRole1011).priority(null).fnAppId(app7).build();
    FnUserRole userRole39 = FnUserRole.builder().userId(fnUser12).roleId(fnRole1012).priority(null).fnAppId(app7).build();

    List<FnUserRole> userRoles = new ArrayList<>(Arrays.asList(userRole1, userRole2, userRole2, userRole3, userRole4,
        userRole5, userRole6, userRole7, userRole8, userRole9, userRole10, userRole11, userRole12, userRole13, userRole14,
        userRole15, userRole16, userRole17, userRole18, userRole19, userRole20, userRole21, userRole22, userRole23, userRole24,
        userRole25, userRole26, userRole27, userRole28, userRole29, userRole30, userRole31, userRole32, userRole33, userRole34,
        userRole35, userRole36, userRole37, userRole38, userRole39));

    fnUserRoleService.saveAll(userRoles);


  }
}
