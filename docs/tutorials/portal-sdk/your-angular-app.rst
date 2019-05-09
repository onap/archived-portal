Your Angular app
=========================
Now that you have the existing anuglarJS application working source code, it's time to migrate to the angular latest version.
The portal SDK application kick stated the migration process from angularJS to angular (v6) using the method angular ngUpgrade.

SDK angular application
-----------------------
To start migration, locate the current SDK angular application in :code:`sdk/ecomp-sdk/epsdk-app-overlay/src/main/webapp/ngapp`.

Setup Steps
-----------
1. Install Node.js and angularCLI. Refer https://angular.io/guide/quickstart
2. npm install in the directory `sdk/ecomp-sdk/epsdk-app-overlay/src/main/webapp/ngapp`
3. ng serve should bring you the welcome.html page in your local machine

firstpage.html
--------------
1. template: the existing welcome.html in `sdk/ecomp-sdk/epsdk-app-overlay/src/main/webapp/app/fusion/scripts/DS2-view-models` is the landing page which will be migrated first.
The content of this file will be copied to angular landling page (index.html) in `sdk/ecomp-sdk/epsdk-app-overlay/src/main/webapp/ngapp/src`. But in SDK the default page name index.html renamed to welcome.html, refer the file angular.json in ngapp.
2. style: the existing css content will be copied to the styles.css in `sdk/ecomp-sdk/epsdk-app-overlay/src/main/webapp/ngapp/src`

Boot the angularJS from angular application
---------------------------------------------------
Each of the menu module (ex: admin, report) can separate angularJS application, so scan the entire *.html files in angularJS source for "ng-app" directive and remove them since the angularJS will be booted from angular application
Refer the https://angular.io/guide/ajs-quick-reference to bootstrap steps. ex: SDK ngapp `this.upgrade.bootstrap(document.body, ['abs']);` in app.component.ts

Controller
-------------
The controller controller.js will be migrated to Type Script. When you create a angular component, by default the controller.ts will be created. So need to copy the content of existing controller.js function to controller.ts
Refer the usage.component.ts `sdk/ecomp-sdk/epsdk-app-overlay/src/main/webapp/ngapp/src/app/admin/components/usage`

Data Service
------------
The existing data service data-service.js will be migrated to Type Script. When you create a angular service, the data-service.ts will be created.
Refer the app-http-client.service.ts `sdk/ecomp-sdk/epsdk-app-overlay/src/main/webapp/ngapp/src/app/admin/services`


Template
-------------
All the HTML file should be scanned and replace with angular directives. ex: "ng-if" in angular "*ngIf"
Refer the https://angular.io/guide/ajs-quick-reference to migrate angularJs to angular directives in the static html should be scanned for


Migrationg existing page to the SDK navigatio
---------------------------------------------
TBD


Adding your new page to the SDK navigation
------------------------------------------
TBD

Rollback to angularJs incase if there is blocker
------------------------------------------------
TBD
