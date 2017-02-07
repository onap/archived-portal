![att_logo] 
# ABS Angular library 
A framework of [AngularJS] directives that allow for a plug'n'play development experience. This framework is apart of AT&T [QuickStart].

# Installation

The framework requres [NodeJS] to be installed prior to building.

## Build

If on a Windows machine, you can quickly start the build by invoking Build.bat which in turn calls:

    grunt --gruntfile ng-Gruntfile.js
    grunt --gruntfile Gruntfile.js


If on Linux/Mac machine, you can directly execute the commands above.

## Run the project

### To Run in Netbeans
1. Right Click on the project
2. **Categories->Sources** 
  * Set the value of "Site Root Folder" to "Production/dist"
3. **Categories->Run**
  * Set the value of "Start File" to "ng-ui-elements.html"
  * Set the value of "Web Root" to "/"

## How to contribute

### To Add a New Module nModule
1. Create a file Under the folder *ng-misc* nModule.hbs
  * nModule.hbs to be a copy of ng-misc/module.html

2. open nModule.hbs and replace all the occurance of *module* by *nModule*

3. ADD one entry at the end of the file app\templates\pages\ng-ui-elements.hbs
  * {{globWithContext 'app/templates/partials/ng-ui-elements/nModule.hbs' this}}

### Create the following folder / file structure under the directory app\scripts\ng_js_att
```javascript
app\scripts\ng_js_att\nModule\docs
app\scripts\ng_js_att\nModule\test
app\scripts\ng_js_att\nModule\nModule.js
app\scripts\ng_js_att\nModule\docs\demo.html
app\scripts\ng_js_att\nModule\docs\demo.js
app\scripts\ng_js_att\nModule\docs\dev-notes.html
app\scripts\ng_js_att\nModule\test\nModule.spec.js
```
    
### Any templates used will go under the following folder app\scripts\ng_js_att_tpls
General Development Rules:

1. Directive to have only the logic.

2. Styles to be out of the logic.

3. Markup to be into templates.

# Copyright
PROPRIETARY INFORMATION

_Not for use or disclosure outside of the AT&T family of companies, except under written agreement._


[AngularJS]:http://angularjs.org
[QuickStart]:http://quickstart.att.com
[NodeJS]:http://nodejs.org
[att_logo]:http://www.underconsideration.com/speakup/archives/att_new_logo.jpg
[det_logo]: http://quickstart.web.att.com/images/logo-DETS.png