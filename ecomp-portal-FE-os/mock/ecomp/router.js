'use strict';

let conf = require('../../client/configurations/mock.json');
let router = require('express').Router();
var concatRegex = /\/portalApi\/[\d,\w,\/,:,?]+$/;


//**Mock endpoints goes here:

//User apps home page
router.get(stripEndpoint(conf.api.userApps), function (req, res) {
    res.json(require('./data/applications/user-apps.json'));
});

//User widgets
router.get(stripEndpoint(conf.api.widgets), function (req, res) {
    res.json(require('./data/widgets/widgets.json'));
});

router.post(stripEndpoint(conf.api.widgets), function (req, res) {
    //res.status(409).json([{"errorCode": 1201, "fields": [{"name":"name"},{"name":"url"}]}]).send();
    res.json();
});

router.put(stripEndpoint(conf.api.widgets + '/:widgetId'), function (req, res) {
    res.json();
});

router.delete(stripEndpoint(conf.api.widgets + '/:widgetId'), function (req, res) {
    res.json();
});

//widget validation
router.post(stripEndpoint(conf.api.widgetsValidation), function (req, res) {
    //404 - not found in DB means valid
    res.status(404).send();
});

//Admins view
router.get(stripEndpoint(conf.api.accountAdmins), function (req, res) {
    res.json(require('./data/admins/account-admins.json'));
});

router.get(stripEndpoint(conf.api.availableApps), function (req, res) {
    res.json(require('./data/applications/available-apps.json'));
});

//Header
router.get(stripEndpoint(conf.api.userProfile), function (req, res) {
    res.json(require('./data/user-profile/user-profile.json'));
});

//New Admin + User adding dialogs
router.get(stripEndpoint(conf.api.queryUsers), function (req, res) {
    res.json(require('./data/users/query-users-results.json'));
});

//new admin dialog
router.get(stripEndpoint(conf.api.adminAppsRoles), function (req, res) {
    res.json(require('./data/admins/admin-app-roles.json'));
});

router.put(stripEndpoint(conf.api.adminAppsRoles), function (req, res) {
    res.send();
});

//Users view
router.get(stripEndpoint(conf.api.adminApps), function (req, res) {
    res.send(require('./data/applications/admin-apps.json'));
});

router.get(stripEndpoint(conf.api.accountUsers), function (req, res) {
    res.send(require('./data/users/account-users.json'));
});

//new user view
// router.get(stripEndpoint(conf.api.userAppsRoles), function (req, res) {
//     res.send(require('./data/users/user-apps-roles.json'));
// });
//
// router.put(stripEndpoint(conf.api.userAppsRoles), function (req, res) {
//     res.send();
// });

//apps onboarding view
router.get(stripEndpoint(conf.api.onboardingApps), function (req, res) {
    res.send(require('./data/applications/applications-onboarding.json'));
});

router.post(stripEndpoint(conf.api.onboardingApps), function (req, res) {
    //res.status(409).json([{"errorCode": 1201, "fields": [{"name":"name"},{"name":"url"}]}]).send();
    res.send();
});

router.put(stripEndpoint(conf.api.onboardingApps), function (req, res) {
    res.send();
});

router.get(stripEndpoint(conf.api.functionalMenuForAuthUser), function (req, res) {
    res.json(require('./data/functional-menu/menu-items.json'));
});
router.get(stripEndpoint(conf.api.functionalMenu), function (req, res) {
    res.json(require('./data/functional-menu/menu-items.json'));
});
router.get(stripEndpoint(conf.api.functionalMenuItemDetails), function (req, res) {
    res.json(require('./data/functional-menu/menu-item-details.json'));
});
router.get(stripEndpoint(conf.api.appRoles), function (req, res) {
    res.json(require('./data/functional-menu/app-roles.json'));
});
// create a new menu item
router.post(stripEndpoint(conf.api.functionalMenuItem), function (req, res) {
    res.send();
});
// edit a menu item
router.put(stripEndpoint(conf.api.functionalMenuItem), function (req, res) {
    res.send();
});
// delete a menu item
router.delete(stripEndpoint(conf.api.functionalMenuItem), function (req, res) {
    res.send();
});
router.get(stripEndpoint(conf.api.getFavoriteItems), function (req, res) {
    res.json(require('./data/functional-menu/favorites.json'));
});
// functionalMenuStaticInfo
router.get(stripEndpoint(conf.api.functionalMenuStaticInfo), function (req, res) {
    res.json(require('./data/user-profile/user-static-info.json'));
});
// Portal Admins
router.get(stripEndpoint(conf.api.portalAdmins), function (req, res) {
    res.json(require('./data/portal-admins/portal-admins.json'));
});
// Ping
router.get(stripEndpoint(conf.api.ping), function (req, res) {
    res.json(require('./data/ping/ping.json'));
});


function stripEndpoint(endpoint) {
    return endpoint.match(concatRegex)[0];
}

module.exports = router;
