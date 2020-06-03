// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  "api": {
    "singleAppInfo": "http://www.portal.onap.org:8080/portal-be-os/portalApi/singleAppInfo",
    "singleAppInfoById": "http://www.portal.onap.org:8080/portal-be-os/portalApi/singleAppInfoById",
    "syncRolesFromExternalAuthSystem": "http://www.portal.onap.org:8080/portal-be-os/portalApi/syncRoles",
    "syncFunctionsFromExternalAuthSystem": "http://www.portal.onap.org:8080/portal-be-os/portalApi/syncFunctions",
    "userApps": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userApps",
    "persUserApps": "http://www.portal.onap.org:8080/portal-be-os/portalApi/persUserApps",
    "appCatalog": "http://www.portal.onap.org:8080/portal-be-os/portalApi/appCatalog",
    "accountAdmins": "http://www.portal.onap.org:8080/portal-be-os/portalApi/accountAdmins",
    "availableApps": "http://www.portal.onap.org:8080/portal-be-os/portalApi/availableApps",
    "allAvailableApps": "http://www.portal.onap.org:8080/portal-be-os/portalApi/allAvailableApps",
    "externalRequestAccessSystem": "http://www.portal.onap.org:8080/portal-be-os/portalApi/externalRequestAccessSystem",
    "userProfile": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userProfile",
    "queryUsers": "http://www.portal.onap.org:8080/portal-be-os/portalApi/queryUsers",
    "adminAppsRoles": "http://www.portal.onap.org:8080/portal-be-os/portalApi/adminAppsRoles",
    "adminApps": "http://www.portal.onap.org:8080/portal-be-os/portalApi/adminApps",
    "appsForSuperAdminAndAccountAdmin": "http://www.portal.onap.org:8080/portal-be-os/portalApi/appsForSuperAdminAndAccountAdmin",
    "accountUsers": "http://www.portal.onap.org:8080/portal-be-os/portalApi/app/:appId/users",
    "saveNewUser": "http://www.portal.onap.org:8080/portal-be-os/portalApi/saveNewUser",
    "userAppRoles": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userAppRoles",
    "onboardingApps": "http://www.portal.onap.org:8080/portal-be-os/portalApi/onboardingApps",
    "widgets": "http://www.portal.onap.org:8080/portal-be-os/portalApi/widgets",
    "widgetsValidation": "http://www.portal.onap.org:8080/portal-be-os/portalApi/widgets/validation",
    "functionalMenuForAuthUser": "http://www.portal.onap.org:8080/portal-be-os/portalApi/functionalMenuForAuthUser",
    "functionalMenuForEditing": "http://www.portal.onap.org:8080/portal-be-os/portalApi/functionalMenuForEditing",
    "functionalMenuForNotificationTree": "http://www.portal.onap.org:8080/portal-be-os/portalApi/functionalMenuForNotificationTree",
    "functionalMenu": "http://www.portal.onap.org:8080/portal-be-os/portalApi/functionalMenu",
    "functionalMenuItemDetails": "http://www.portal.onap.org:8080/portal-be-os/portalApi/functionalMenuItemDetails/:menuId",
    "appRoles": "http://www.portal.onap.org:8080/portal-be-os/portalApi/appRoles/:appId",
    "appThumbnail": "http://www.portal.onap.org:8080/portal-be-os/portalApi/appThumbnail/:appId",
    "functionalMenuItem": "http://www.portal.onap.org:8080/portal-be-os/portalApi/functionalMenuItem",
    "regenerateFunctionalMenuAncestors": "http://www.portal.onap.org:8080/portal-be-os/portalApi/regenerateFunctionalMenuAncestors",
    "listOfApp": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getAppList",
    "setFavoriteItem": "http://www.portal.onap.org:8080/portal-be-os/portalApi/setFavoriteItem",
    "getFavoriteItems": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getFavoriteItems",
    "removeFavoriteItem": "http://www.portal.onap.org:8080/portal-be-os/portalApi/removeFavoriteItem/:menuId",
    "ping": "http://www.portal.onap.org:8080/portal-be-os/portalApi/ping",
    "functionalMenuStaticInfo": "http://www.portal.onap.org:8080/portal-be-os/portalApi/functionalMenuStaticInfo",
    "portalAdmins": "http://www.portal.onap.org:8080/portal-be-os/portalApi/portalAdmins",
    "portalAdmin": "http://www.portal.onap.org:8080/portal-be-os/portalApi/portalAdmin",
    "getManifest": "http://www.portal.onap.org:8080/portal-be-os/portalApi/manifest",
    "getActiveUser": "http://www.portal.onap.org:8080/portal-be-os/portalApi/dashboard/activeUsers",
    "getSearchAllByStringResults": "http://www.portal.onap.org:8080/portal-be-os/portalApi/dashboard/search",
    "commonWidget": "http://www.portal.onap.org:8080/portal-be-os/portalApi/dashboard/widgetData",
    "deleteCommonWidget": "http://www.portal.onap.org:8080/portal-be-os/portalApi/dashboard/deleteData",
    "getContactUS": "http://www.portal.onap.org:8080/portal-be-os/portalApi/contactus/list",
    "getAppsAndContacts": "http://www.portal.onap.org:8080/portal-be-os/portalApi/contactus/allapps",
    "saveContactUS": "http://www.portal.onap.org:8080/portal-be-os/portalApi/contactus/save",
    "deleteContactUS": "http://www.portal.onap.org:8080/portal-be-os/portalApi/contactus/delete",
    "getContactUSPortalDetails": "http://www.portal.onap.org:8080/portal-be-os/portalApi/contactus/feedback",
    "getAppCategoryFunctions": "http://www.portal.onap.org:8080/portal-be-os/portalApi/contactus/functions",
    "onlineUserUpdateRate": "http://www.portal.onap.org:8080/portal-be-os/portalApi/dashboard/onlineUserUpdateRate",
    "storeAuditLog": "http://www.portal.onap.org:8080/portal-be-os/portalApi/auditLog/store",
    "leftmenuItems": "http://www.portal.onap.org:8080/portal-be-os/portalApi/leftmenuItems",
    "getFunctionalMenuRole": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getFunctionalMenuRole",
    "getNotifications": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getNotifications",
    "getMessageRecipients": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getMessageRecipients",
    "getRecommendations": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getRecommendations",
    "getAdminNotifications": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getAdminNotifications",
    "getAllAppRoleIds": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getNotificationAppRoles",
    "getNotificationHistory": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getNotificationHistory",
    "notificationUpdateRate": "http://www.portal.onap.org:8080/portal-be-os/portalApi/notificationUpdateRate",
    "notificationRead": "http://www.portal.onap.org:8080/portal-be-os/portalApi/notificationRead",
    "saveNotification": "http://www.portal.onap.org:8080/portal-be-os/portalApi/saveNotification",
    "getNotificationRoles": "http://www.portal.onap.org:8080/portal-be-os/portalApi/notificationRole",
    "getRole": "http://www.portal.onap.org:8080/portal-be-os/portalApi/get_role",
    "getRoles": "http://www.portal.onap.org:8080/portal-be-os/portalApi/get_roles/:appId",
    "toggleRole": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role_list/toggleRole",
    "removeRole": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role_list/removeRole",
    "saveRole": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role/saveRole/:appId",
    "toggleRoleRoleFunction": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role/removeRoleFunction.htm",
    "addRoleRoleFunction": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role/addRoleFunction.htm",
    "toggleRoleChildRole": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role/removeChildRole.htm",
    "addRoleChildRole": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role/addChildRole.htm",
    "getRoleFunctions": "http://www.portal.onap.org:8080/portal-be-os/portalApi/get_role_functions/:appId",
    "saveRoleFunction": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role_function_list/saveRoleFunction/:appId",
    "removeRoleFunction": "http://www.portal.onap.org:8080/portal-be-os/portalApi/role_function_list/removeRoleFunction/:appId",
    "userAppsOrderBySortPref": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userAppsOrderBySortPref",
    "userAppsOrderByName": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userAppsOrderByName",
    "saveUserAppsSortingPreference": "http://www.portal.onap.org:8080/portal-be-os/portalApi/saveUserAppsSortingPreference",
    "userAppsSortTypePreference": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userAppsSortTypePreference",
    "userAppsOrderByLastUsed": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userAppsOrderByLastUsed",
    "userAppsOrderByMostUsed": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userAppsOrderByMostUsed",
    "userAppsOrderByManual": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userAppsOrderByManual",
    "saveUserAppsSortingManual": "http://www.portal.onap.org:8080/portal-be-os/portalApi/saveUserAppsSortingManual",
    "saveUserWidgetsSortManual": "http://www.portal.onap.org:8080/portal-be-os/portalApi/saveUserWidgetsSortManual",
    "updateWidgetsSortPref": "http://www.portal.onap.org:8080/portal-be-os/portalApi/updateWidgetsSortPref",
    "UpdateUserAppsSortManual": "http://www.portal.onap.org:8080/portal-be-os/portalApi/UpdateUserAppsSortManual",
    "widgetCatalogSelection": "http://www.portal.onap.org:8080/portal-be-os/portalApi/widgetCatalogSelection",
    "widgetCommon": "http://www.portal.onap.org:8080/portal-be-os/portalApi/microservices",
    "appCatalogRoles": "http://www.portal.onap.org:8080/portal-be-os/portalApi/appCatalogRoles",
    "saveUserAppRoles": "http://www.portal.onap.org:8080/portal-be-os/portalApi/saveUserAppRoles",
    "userApplicationRoles": "http://www.portal.onap.org:8080/portal-be-os/portalApi/userApplicationRoles",
    "microserviceProxy": "http://www.portal.onap.org:8080/portal-be-os/portalApi/microservice/proxy",
    "getUserAppsWebAnalytics": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getUserAppsWebAnalytics",
    "getWebAnalyticsOfApp": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getWebAnalyticsOfApp",
    "basicAuthAccount": "http://www.portal.onap.org:8080/portal-be-os/portalApi/basicAuthAccount",
    "addWebAnalyticsReport": "http://www.portal.onap.org:8080/portal-be-os/portalApi/addWebAnalyticsReport",
    "getUserJourneyAnalyticsReport": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getUserJourneyAnalyticsReport",
    "deleteWebAnalyticsReport": "http://www.portal.onap.org:8080/portal-be-os/portalApi/deleteWebAnalyticsReport",
    "getAllWebAnalytics": "http://www.portal.onap.org:8080/portal-be-os/portalApi/getAllWebAnalytics",
    "modifyWebAnalyticsReport": "http://www.portal.onap.org:8080/portal-be-os/portalApi/modifyWebAnalyticsReport",
    "appsFullList": "http://www.portal.onap.org:8080/portal-be-os/portalApi/appsFullList",
    "portalTitle": "http://www.portal.onap.org:8080/portal-be-os/portalApi/ecompTitle",
    "centralizedApps": "http://www.portal.onap.org:8080/portal-be-os/portalApi/centralizedApps",
    "getSchedulerId": "http://www.portal.onap.org:8080/portal-be-os/portalApi/post_create_new_vnf_change",
    "getTimeslotsForScheduler": "http://www.portal.onap.org:8080/portal-be-os/portalApi/get_time_slots",
    "postSubmitForApprovedTimeslots": "http://www.portal.onap.org:8080/portal-be-os/portalApi/submit_vnf_change_timeslots",
    "getPolicy": "http://www.portal.onap.org:8080/portal-be-os/portalApi/get_policy",
    "getSchedulerConstants": "http://www.portal.onap.org:8080/portal-be-os/portalApi/get_scheduler_constant",
    "uploadRoleFunction": "http://www.portal.onap.org:8080/portal-be-os/portalApi/uploadRoleFunction/:appId",
    "checkIfUserIsSuperAdmin": "http://www.portal.onap.org:8080/portal-be-os/portalApi/checkIfUserIsSuperAdmin",
    "getCurrentLang": "http://www.portal.onap.org:8080/portal-be-os/auxapi/languageSetting/user/:loginId",
    "getLanguages": "http://www.portal.onap.org:8080/portal-be-os/auxapi/language",
    "updateLang": "http://www.portal.onap.org:8080/portal-be-os/auxapi/languageSetting/user/:loginId",
    "linkQ": "",
    "linkPic": "",
    "brandName": "ONAP Portal",
    "brandLogoImagePath": "",
    "footerLink": "",
    "footerLinkText": "",
    "footerMessage": "",
    "footerLogoImagePath": "",
    "footerLogoText": "",
    "intraSearcLink": "",
    "extraSearcLink": "https://wiki.onap.org/dosearchsite.action?cql=siteSearch+~+searchStringPlaceHolder&queryString=searchStringPlaceHolder",
    "browserCompatibilityMsg": "This Application is best viewed on Firefox, Chrome and Edge."
  },
  "getAccessUrl": "",
  "getAccessName": "",
  "getAccessInfo": "",
  "cookieDomain": "onap.org"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
