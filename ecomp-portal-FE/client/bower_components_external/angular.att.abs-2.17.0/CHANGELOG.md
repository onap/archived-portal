<a name"2.17.0"></a>
## 2.17.0 (2015-11-23)


#### Bug Fixes

* **attSelect:** CATO tweak and cleanup (6d54d467)
* **devNotes:** devNotes IE 8 html rendering issue fixed (e80121b3)
* **megamenu:**
  * Removed dead code (306f0afa)
  * accessibility fixes (27226d07)
  * demo with toggle menu (3847451c)
  * CSS tweaks (81becdab)


#### Features

* **attSelect:**
  * Removed Dead code. (793e9b37)
  * Fix disabled dropdown CATO + unified styles (e7532e03)
* **modals:** NVDA now reads the dialog title (e2f771e5)


<a name"2.16.0"></a>
## 2.16.0 (2015-11-18)


#### Bug Fixes

* **attBoardStrip:** Clicking fast on prev/next icons doesnt highlight (0fc2dcb1)
* **attBoardstrip:** Tabindex issue (8aae40fd)


#### Features

* **attBoardStrip:**
  * Index bound checking (28389fc6)
  * CATO fixes. (7cf74942)
  * 508 enhancement (c7b2f05c)
  * Unit tests (b1f93771)
  * Refactor variables into constants (8b1fae60)
  * Dev can now set selected board on load. (9fb96882)
  * Added some missing code. (4e1c0a4c)
  * Keyboard Accessibility (9bd74ab0)
* **attBoardstip:** Cleanup (959c1191)
* **attBoardstrip:** Remove dead code. (e9551723)
* **boardStrip:** added animation (bf03b428)
* **ieshiv:** Added addEventListener for IE6+ (ab0e6f1d)
* **infiniteScroll:** Fallbacks added (5bba2e7c)
* **tooltip:** Dev can now override tooltip tabindex (3852a2aa)
* **utilities:**
  * Merge conflicts (c5ce83f0)
  * Added app.js (9de1b4e2)
  * Cleaned up some code. (f6802757)
* **utilties:**
  * horizontal scrolls are CATO (b4ebaaba)
  * Updated dev-notes. (50043d90)
* **utiltites:**
  * threshold is now configurable (94bc9ada)
  * merge conflicts (be0ecb6c)
  * Styles are completed. (5592303c)
* **whenScrollEnds:**
  * width and height can be overriden. (b9f5cae9)
  * PR comments (60446b68)
  * Merged into one directive again (b9176dd5)
  * added two variants of infinite scroll (7a7a9135)


<a name"2.15.0"></a>
## 2.15.0 (2015-11-17)


#### Bug Fixes

* **Freestanding:** focus was not visible on freestanding issue fixed (5f092788)
* **accordion:** tag was not closed properly (2bf4c421)
* **attBtnDropdown:** attBtnDropdown NVDA IE issue fixed (b2f6cdf7)
* **attSelect:**
  * select reset to placeholder if passed -1 (16435973)
  * update test spec (892acc40)
  * change the tag name (86fa9882)
  * attSelect  IE nvda issue fixed (f79cc88d)
* **attTable:**
  * Headers desc col content (e06f9191)
  * Headers now desc if sortable and pattern (945f9812)
* **checkBoxcss:** Remove unused css (e69b1c68)
* **docs:** updated demo (91fd5070)
* **megaMenu:**
  * updated tab scss (3eacda71)
  * Fixed Accessibilty (2e2b7b3a)
* **path:**
  * bas css path reference (9949745f)
  * change path reference (a6ec368a)
* **pathRefernce:** Change reference path (430f8840)
* **referencePath:** images replaces with $att-images-base-path (6779a316)
* **search:** Search component IE nvda reading issue fixed (bc961800)
* **searchSelect:**
  * fix for DE164769 (db6f9a27)
  * NVDA fix DE164769 (c981d628)
* **slider:** NVDA fix DE164780 (45821c48)
* **tabs:** MegaMenu traingle update(tabs): att-globe image added in MegaMenu (407c8c64)


#### Features

* **MegaMenu:**
  * 508 complaint (300e5b16)
  * Accessibility Improvements (313f244f)
* **attCheckbox:** Minor cleanup (d6ec4657)
* **icons:** Iconography now has search and is data-driven (1a2c6ea6)
* **ieShiv:** added directive in ieShiv to fix IE 8 issue (9df315e9)


<a name"2.14.0"></a>
## 2.14.0 (2015-11-03)


#### Bug Fixes

* **colorSelector:**
  * Updates as per comment in PR 775 (1741b0d6)
  * Updates as per comment in PR 775 (ed58f26c)
* **conflict:** merge branch v1.2 into feature/nitesh-messages-revamp (061a92b8)
* **formField:**
  * made messageType attr optional (19356138)
  * updated form field to consume message-type (b05c414b)
* **message:**
  * made messageType attr optional (8f82ff63)
  * minor semicolon cleanup (78ddf52f)
  * updated att-messages to expose message-type (dc4fe8ef)
* **tagBadges:** NVDA fix (b7baf0c6)


#### Features

* **Update:**
  * Remove semicolon from meta tag (4e96b643)
  * Update splitIcon remove title (aae56524)
  * Update split buttons role for ul (086bfe6b)
  * Update buttons to follow guided automatics. (3b899c94)
  * color selector removed reserved keyword from html (4b9aa3c6)
  * color selector removed reserved keyword from scope. (13e7f872)


<a name"2.13.0"></a>
## 2.13.0 (2015-10-29)


#### Bug Fixes

* **attSelect:**
  * removed dead code. (123e5e13)
  * PR comments (a8c4b64e)
  * Code cleanup (c3fc63bd)
  * Selection under strict filter works in all cases. (e8dac0ad)
  * On open, dropdown now properly selects element (5d9f715a)
  * Keyboard navigation is now aware of filtering (7e00ea20)
* **attUserMessage:**
  * Removed alt tag (59cc7acd)
  * removed bad attribute (67f69e8a)
* **build:** Resolve conflict (a74d4d0f)
* **colorSelector:** Fix for defect DE164655 (db998406)
* **conflict:**
  * Merged & solved conflicts with v1.2 (ce220d81)
  * Merged & solved conflicts with v1.2 (fe3c0096)
* **datepicker:**
  * improved NVDA experience (d26b12ef)
  * removed unnecessary id for accessibility (d82230dc)
  * Removed space (7c24ab17)
  * NVDA now reads dates when tabbing through (908c87e0)
* **forms:**
  * demo.html updated as per feedback from Joe fix(forms): Dev Notes updated (a9dc1af6)
  * demo.html updated (3f31a67b)
  * Cedit card validation updated (b958b3dd)
  * Auto credit card reorganization removed temporarily (64328735)
* **misc:**
  * added colorSelctorWrapper in IE-shiv (a7ceccfe)
  * Incorporate with the changes as per comment in PR-765 (aa6c5d2f)
* **modal:**
  * Ensured that other input types can backspace (83f16758)
  * Code cleanup (062f631a)
  * Modals now prevent backspace navigating back. (d22a5a18)
* **paneSelector:**
  * update changes as per comment in PR-765 (c7e226aa)
  * code clean Up (d0060a67)
  * devnotes updated (d715863d)
  * Code Clean up and test Specs (a312d573)
* **phoneMask:**
  * textual changes as per Joe comment (0c784a0d)
  * changes in model value on click (821b81c4)
* **phonemask:**
  * incorporate with the changes as per comment in PR-765 (843b56a3)
  * incorporate with the changes as per comment in PR-765 (3283c5b9)
  * controller updated (ead01692)
* **select:** fixed dropdown IE8 nvda issue (64a8809e)
* **stepTracker:**
  * code cleanup (cada1c07)
  * fix for defect DE164808 (a111a45a)
* **utilities:** console.log statement removed (9d7be007)


#### Features

* **DOMHelper:**
  * Simplified code. (375403e7)
  * Return undefined instead of false. (57b7636f)
  * Enhanced DOMHelper to find our directives as well (02217555)
* **Update:** bugfix/attselect-508-fixes-ss resolving conflicts (55d90b4b)
* **attSelect:** Removed code not yet used. (96d6ca24)
* **attUserMessages:** added 508 compliance (7ec3483f)
* **boardStrip:**
  * updated ie-shiv (e7ea35fa)
  * incorporated review comments (cf3b3ded)
  * updated CSS (f9bf14a9)
  * updated plain CSS to SCSS (dc051d66)
  * updated code per the latest requirements (dae8fb67)
  * updated code per the latest requirements (5fd053a7)
  * updated code per the latest requirements (028ec962)
* **popover:**
  * Updated code + ehanced DOMHelper (f135582b)
  * Popover now has toggleable close button (930616b5)
* **resolve:** resolve conflicts with release 1.2 branch (484d2b14)
* **update:**
  * Update components for E508 compliance guided automatics (9b0deab3)
  * Update components for E508 compliance guided automatics (de7d0058)
  * Update components for E508 compliance (835fda4e)
* **utilities:**
  * Added DOMHelper for finding first tabable. (a87cfbbe)
  * New service to help gain info about browser (45f01be2)
* **utiltites:** We can now find first tabable element in all cases. (4afd735c)


<a name"2.12.0"></a>
## 2.12.0 (2015-10-15)


#### Bug Fixes

* **attRadio:**
  * added value attr from JS (56e7ae16)
  * reusing variable (069bdcfc)
  * Updated test specs (ef097170)
  * fixed nvda issue (77bfe7a8)
* **attSelect:** Fixed regression for highlight/focus not synced (32ebad30)
* **checkboxgroup:** Group now retains previous model states (4c25ceb7)
* **docs:** updated demo.html (b9020a9a)
* **paneSelector:**
  * Code CleanUp (3e21470f)
  * Removed chevron from lastPane (285db4c5)
  * fix for sonar issues (dc3a5d20)
* **phoneMask:** phone mask validation fix (d98ad19c)
* **popOvers:** updating the partial name (d7e35e39)
* **styles:**
  * Made property more browser agnostic (26820a1c)
  * Placeholder text is not consistent between browsers. (fd3d3fda)
* **toggle:**
  * Removed extra spaces (dec78d72)
  * cross browser NVDA issue fixed (9f4fc2d4)


#### Features

* **paneSelector:** keyboard Accessibilty in pane selector (9fe8014e)


<a name"2.11.1"></a>
### 2.11.1 (2015-09-30)


#### Bug Fixes

* **dragDrop:** dev notes updated (880a5ed9)
* **formfield:** ie8 issue fixed (c4e938f4)
* **link:** Keyboard accessibility added in ReadMore link (daaeb954)
* **select:** Dropdown with no placeholder issue resolved (512cbc74)
* **tooltip:** Fixed flashing tooltip on click of input field (7338631f)


#### Features

* **popovers:** initial commit (dbeec1b4)


<a name"2.11.0"></a>
## 2.11.0 (2015-09-24)


#### Bug Fixes

* **Select:** E508 related udpates fix(Search): E508 related udpates (c9a46a3c)
* **button-minWidth:** update image path and add button min-Width (0f37f646)
* **dragDrop:** Resolve issue for selecting same file twice (db409be1)
* **forField:** code beautification (dfca05bc)
* **formField:**
  * added note for IE fix (ec13e376)
  * note added for IE fix (83f313bf)
  * IE fixes (767e618a)


<a name"2.10.0"></a>
## 2.10.0 (2015-09-18)


#### Bug Fixes

* **buttonLoader:** Animation fixes for IE (dbb15915)
* **checkbox:** Firefox Focus outline issue fixed (384b56cb)
* **docs:**  datepicker filter from date issue fixed (14c09533)
* **slider:**
  * keyboard  focus issue fixed (1cc798ea)
  * keyboard  focus issue fixed (63e81d9d)
* **stepTracker:** ariaLabel added (0b13b316)
* **textDropdown:** IE issue fixed (d9e9ab90)


#### Features

* **modal:** TN selector add/remove all work under filtering. (c2cc992b)


<a name"2.9.0"></a>
## 2.9.0 (2015-09-16)


#### Bug Fixes

* **select:** typo (ab0716ff)
* **splitButtonDropdown:** Commented out tests to get a build (b4ecb630)


<a name"2.8.0"></a>
## 2.8.0 (2015-09-15)


#### Bug Fixes

* **attButtonDropdown:**
  * Fixed test (231a3a3f)
  * CATO fixes (8d723500)
* **attSelect:**
  * revert back my previous changes, wrong branch (39bd919c)
  * added unit test case (b67ec5b7)
* **datepicker:** updated and cleaned $render function of dateFilter (70e2dad6)
* **docs:**  changed placeholder as option attr for demo (ac22bbaf)
* **select:** have placeholder as option with no filter (713bf1d3)
* **stepSlider:**
  * added unit test case (6766f6fd)
  * This is just to add the previous fix given by Mohit to show the lower range valu (d46a12fa)
  * Updated the conversion to truncate values instead of rounding off (10d71136)
  * removed the extra spaces in the dimension so it wont confused devs on why an ext (3667b46f)
  * Replace double quotes with single quotes (233229cd)
  * Replace double quotes with single quotes (24563d2d)
  * I forgot to add the default value in else (630e87ce)
  * Updated test cases as per review comments (062360b0)
  * Updated the code as per review.  Created a function for reusability (82f0e9a6)
  * Added conversion of units and values for the tooltip and endDimension (1eeaead9)
  * Added conversion on the slider Tooltip (2ba0a515)
* **tooltips:** resolved merge conflicts (ac593791)


#### Features

* **FormFieldPrv:**
  * ascii codes moved (4b249f7c)
  * sonar and code reveiew (22e88bde)
* **PreventKeyStrokeDirective:**
  * adding more test cases (04565c16)
  * commiting new file (a5c27cff)
  * commiting some more changes (6db12c74)
* **attButtonDropdown:**
  * Updated for PR comments (ee282cf9)
  * CATO implemented. (a624dce2)
* **attFormFieldPrv:** code review changes (2f1cabcd)
* **hourpicker:** ability to define pre-select day-wise values (ef4c2915)
* **tooltips:** performance related fix (f4504cdd)


<a name"2.7.0"></a>
## 2.7.0 (2015-09-02)


#### Bug Fixes

* **Gruntfile:** Spaces deleted (b65d173c)
* **Selected:** Code clean up (b99858b9)
* **docs:**
  * updated demo.js  for button and modal (9f2b8fb7)
  * added json condition in partials (7f5e2547)
* **select:** remove unused css (176b07b0)
* **tabbedOverlay:** Given spaces between variable (8c141461)
* **table:**
  * fixed issue for table (2476c028)
  * Added demo.json as 3rd pane for json (bf32862b)


#### Features

* **Gruntfile:** added task for json pane (8d5ae0a8)
* **attSearch:** Updated to latest select code and deprecated (05e86677)
* **button:** created demo.json (ce6a5ca1)
* **devNotes:** update devnotes for json (0dd5b43e)
* **select:**
  * Dev Notes updated for dropdown with disabled items (642e203f)
  * Dropdown with disabled options implemented (2b384c04)
* **tabbedOverlay:** Created demo.json (3c2b4589)


<a name"2.6.0"></a>
## 2.6.0 (2015-08-25)


#### Bug Fixes

* **Leftnav:** extra 'e' removed from bottom of menu (a98a2bdf)
* **MenuSearch:** cActive variable issue fixed (7dc5d3cc)
* **Select:** No placeholder - on enter empty selected - fixed fix(Select): No placeholder - s (bdfbd54e)
* **checkbox:**  reverted sonar fix (46ed2e09)
* **datepicker:** updated dateFilter viewport width to 100% from fixed (98fcfa96)
* **modal:** Added  to controller (f94848fc)
* **modals:**
  * reverting sonar suggested changes (64b0bac1)
  * reverting sonar suggested changes (4db44dea)


#### Features

* **hourpicker:** added attHourpickerValidator directive (a2378b7a)
* **modals:**
  * documentation update (ff192f1a)
  * focus comes back to original element (39edf55e)
* **splitIconButton:**
  * remove dead DI (cbe2c0ae)
  * removed logs (fbb28962)
  * CATO + Sonar (2a71d715)
  * Sonar and misc formatting (9e9dfcc3)
* **tooltips:** tooltips offset and custom aria-label (3a0f59ab)


<a name"2.5.0"></a>
## 2.5.0 (2015-08-18)


#### Bug Fixes

* **buttons:** Fix broken spec on phantomjs (c8214655)
* **utilties:** Comment unit test for phantomjs issue (d2df1eb5)


<a name"2.4.0"></a>
## 2.4.0 (2015-08-17)


#### Bug Fixes

* **attSelect:**
  * Fixed viewport shifting (12642663)
  * No filter properly scrolls (a10c7c8a)
  * Remove statesdropdown + fix selection bug (8f751b4e)
  * Fixed hover edge case (d7d35cb5)
* **docs:**
  * added sample JSON structure. (8f2970f5)
  * update demo.css (c6a18a07)
  * move internal css into demo.css (446062a5)
* **modal:**
  * number selection DE147479 (931294f9)
  * number selection - filtered selection works again (2fb1a16d)
  * number selection 0th element selectable (1edbceb9)
* **textDropdown:**
  * CATO Fixes (1b5af417)
  * CATO fixes (5e877d9b)
  * Fix cato issue (11fcaddd)


#### Features

* **Build:**
  * moved release notes to generalbuild (9644ec01)
  * Moved ReleaseTask.js to GeneralBuild (03f460ed)
* **attBtnDropdown:**
  * unit test (ced18db2)
  * Updated styles, CATO/508 (dd246bb2)
* **attDrawer:**
  * Merge conflicts (d867e5f1)
  * Unit Tests (7532294a)
  * CATO (cea7e85d)
* **attSelect:**
  * remove log statement (e3914d62)
  * Merge conflicts (c4499b19)
  * Fix demo css (fa2e7de1)
  * Fix unit test issues (788ff03f)
  * merge conflicts (3aebfe33)
  * Fixed test being iit'd (8e79dfd0)
  * Fixed test for focus retention (a05464b6)
  * Merge conflicts (7cface48)
  * Sonar fixes (f7ecc1a8)
  * Remove showInputFilter for CATO (3cfa0084)
  * Focus returns to dropdown after close. (afb6ed86)
  * Now allows jumping to elements without being open (0ac16cab)
  * Search resets if keystrokes are less than 100ms apart (b36c9a31)
  * Searching loops through list elements (6b6a2741)
  * Auto select based on first 2 characters typed (8fdd1926)
  * Added API to remove all filtering + focus fix (ba8ee5af)
* **attTableMessages:** CATO support (c1431169)
* **attTimeline:**
  * CATO + documentation update (80d91bff)
  * CATO + documentation update (35533a51)
* **attUserMessage:** CATO - focus shift + screen reader (86ed26a5)
* **attUsermessage:** Unit tests (282690bb)
* **focusOn:** Added unit tests + Sonar improvements (dfbf8f20)
* **modal:**
  * number selection cato enhancements (5a16d19f)
  * merge conflicts (dc5992b1)
  * number selection - ordering is retained btw lists (6b6f43ab)
  * Number Selection Updates (67de94cc)
  * Number Selection now has shift selection support (61167314)
* **select:** merge conflicts (356df768)
* **table:**
  * http call to get jsondata (072b26d6)
  * moved table data to json file (0d68080d)
* **taboverlay:** http call to get jsondata (e14827af)
* **textDropdown:** Sonar fixes (6019b7c3)
* **timeline:** Merge conflicts (1525e9e4)


<a name"2.3.0"></a>
## 2.3.0 (2015-08-07)


#### Bug Fixes

* **accordHeader:**
  * events typo (58d8b277)
  * using keymap from utilities now (a115268a)
  * CATO for customizable accordion (290b3516)
* **modal:** fixed modal scroll issue (1adcd506)


#### Features

* **formfield:** Removed unnecessary fields (eb59d54e)
* **release:** v2.3.0 (d9259235)
* **statesDropdown:** Updated to allow passing in custom data (1ac882be)


<a name"2.2.0"></a>
## 2.2.0 (2015-08-04)


#### Bug Fixes

* **Logs:** To add changelog in bower (765f8ee3)
* **modal:**
  * Fix number selection IE function (3bd6862c)
  * Ensure close icon is closable (0c5fca50)
  * Added a tabindex on close. (717d1a30)
  * Number selection template redlines (3f1b90f1)
* **treeView:** fixed $ not found issue (358aa342)


#### Features

* **build:** Added a demo specific css file. (0de7cd23)
* **formfield:** updated form field validations and test spec (c3753141)
* **modal:** Merge conflicts (df1f16f9)
* **release:** v2.2.0 (0329259b)
* **statesDropdown:**
  * CATO fixes + AInspector (3603b9a3)
  * Unit tests + optimization (a6d0bb81)
  * Added new dropdown only for states. (f1f2f135)
  * Unit tests (e05d28a2)


<a name"2.1.0"></a>
## 2.1.0 (2015-07-31)


#### Bug Fixes

* **Jenkins:** fixed issues due to structural changes (47442d76)
* **attBtnDropdown:**
  * Added alt as recommended on review (509950d3)
  * Updated the title from Circle to Click fo r Options (3852fe93)
  * Updated the title from Circle to Click for Options (78324fdb)
* **megaMenu:**  resolved accessibility issue. (83a871c1)
* **paneSelector:** Resolved issue pane not opening (fbdc21f6)
* **testSpec:** Updated test specs as per TestSpecHelper (62b3db9c)
* **testSpecHelper:**
  * Resolved conflicts (b097d746)
  * Resolve conflict (6cf46d10)
* **testSpecs:** updated test specs for utilities (1e4b80df)


#### Features

* **formfield:**
  * updated form field validations (c0ecd815)
  * updated phone mask validation (457a56d1)
* **release:** v2.1.0 (916578d5)
* **testSpecHeelper:** testSpecHelper added (3a25124a)
* **testSpechelper:**
  * apply testspechelper method on all testspec (dd500345)
  * implemented testspechelper method (17a75fad)


<a name"1.2.54"></a>
### 1.2.54 (2015-07-17)


#### Bug Fixes

* **AInspector:**
  * Header.Tables,Wigets/Scripts (0e78a531)
  * remove Id attribute from template (95a44e82)
  * Toggle test specs updated (64f159a9)
  * Landmark,links, forms,audio/video (b26d6c07)
* **Ainspector:**
  * Aria-labels modified (a64a9e0e)
  * incorporate the changes as per comments (0d94f89f)
* **Banner:** Fixed issues in banner (2745eeae)
* **ainspector:** landmork issue resolve (4a474168)
* **boardStrip:** resolve conflict (82b328d5)
* **build:** Changelog.md updated (7dc18a12)
* **tab:** made require parentTab optional (7863d026)
* **table:**
  * reverted arrow-direction and related functionality (6c868ef8)
  * updated highlighting logic for sorting issue fix (f429b595)
* **typeAhead:** code clean Up (54fc8567)


#### Features

* **Jenkins:** changes in ReleaseTask and chan (0a16d2ff)
* **release:** v1.2.54 (da4d64d8)


<a name"1.2.54"></a>
### 1.2.54 (2015-07-09)

## Features

## Bug Fixes

- **Accessibilty Fixes on whole library as per AInspector ruleset **


<a name"1.2.53"></a>
### 1.2.53 (2015-06-29)


#### Features

* **release:** v1.2.53 (3b3f3957)


<a name"1.2.52"></a>
### 1.2.52 (2015-06-26)


# 1.2.52

## Features

- **FocusOn**
  - Added a new directive which can shift focus based on some condition.

- **attCarousel**
  - New directive that allows adding of boards with custom images and navigating through them.

- **attTable**
  - Performance enhancement for handling large sets of data. New track-by attribute which, given a unique key in data, will speed up rendering. 

- **splitIconButton**
  - Module att.abs.splitButtonDropdown split into two parts - splitButtonDropdown and splitIconButton

- **Scrollbar**
  - Added horizonzal scrollbar functionality to existing scrollbar.

- **Position**
  - Added `att-position` directive with similar functionality of `$position` service.

- **Drag and Drop**
  - Added try catch with broadcast event 'att-file-link-failure', in case user do not follow the guidelines mentioned on Sandbox demo page for  IE8/IE9 browser.

## Bug Fixes

- **typeAhead**
  - browser default tooltip issue resolved due to title attribute.

- **attStepSlider**
  - Removed timeout so slider will render without delay
  - Fixed missing realtimeCallback functionality

- **Sonar Fixes related to JS files for following mention Components**
  - Alert  
  - Checkbox
  - radio
  - profilecards
  - color-selector
  - typeahed
  - modal
  - formfield
  - treeview
  - videocontrols
  - formfield
  - usermessage
  - Link
  - Buttons
  - steptracker
  - tabs
  - dragdrop
  - breadCrumbs
  - tagBadges
  - tooltips
  - accordion
  - utilities
  - hourpicker
  - dividerlines
  - devNotes
  - position
  - textOverflow
  - videoControls
  - pagination
  - drawer
  - verticalSteptracker
  - message
  - accordion
  - utilities
  - table
  - select
  - loading
  - paneSelector
  - search
  - transition

- **Sonar Fixes related to Test Coverage for following mention Components**
  - Alert  
  - Checkbox
  - radio
  - profilecards
  - color-selector
  - typeahed
  - modal
  - formfield
  - treeview
  - videocontrols
  - formfield
  - usermessage
  - Link
  - Buttons
  - steptracker
  - tabs
  - dragdrop
  - breadCrumbs
  - tagBadges
  - tooltips
  - accordion
  - utilities
  - hourpicker
  - dividerlines
  - devNotes
  - position
  - textOverflow
  - videoControls
  - pagination
  - drawer
  - verticalSteptracker
  - message
  - accordion
  - utilities
  - table
  - select
  - loading
  - paneSelector
  - search
  - transition

- **accordion**
  - Code and performance improvements
  - Improvements from accessibility perspective

- **tooltip**
  - Code and performance improvements
  - Removed delay in timeout in tooltipCondition directive

## Breaking Changes



## Deprecated API

- **typeAhead**

 - title attribute is deprecated and title-name attribute is used instead as it is conflicting with html title attribute.
 - for backward compatibility  we can still use title attribute, but will be deprecated in future major release.
 - Example:
 -<div type-ahead  items="items" title="name"/>(deprecated).
 -<div type-ahead  items="items"  title-name="name"/>.

# 1.2.48

## Features

- **datepicker**
  - Added child directive for to add date filter custom list
 
- **attCheckbox**
  - Added code for max selectable checkboxes.

## Bug Fixes

- **Text Dropdown**
  - Highlight fix for CATO.

- **Toggle Switch**
  - Hightlighting fix on focus for CATO.

- **Att Table Body**
  - Decrease timeout

- **Att Step Slider**
  - Increase precision range to fix scale fill not same location as pointer

- **Att Select**
  - Fix bug where user was unable to click on the dropdown default option again

## Breaking Changes


## Known Issues
- **Dropdowns**
    - NVDA reads auto-complete dropdown results as "blank"



# 1.2.47

## Features

- **Month Filter**
  - Implemented clear months.

## Bug Fixes

- **Toggle**
  - Toggle no longer requires its controller to be named toggleController.


## Breaking Changes




# 1.2.46

## Features

- **Month Filter**
  - Implemented two way binding in month filter's model.
  - Resolved disabled month selection issue.
  - Disabled last 6 and 12 months temporarily.

- **Att Flexible Accordion (attAccord)**
 -  Create new flexible accordion allowing user to add own markup to header and body

## Bug Fixes

- **attTable**
  -Added scope paramters for arrowDirection and clickFunc to control 
  the direction of the highlighted arrows.
  
- **Split Icon Button Dropdown**
  - NVDA Fix

- **Accordion Multipurpose Expander**
  - Expand/Collapse through Keyboard fixed
  - Customer HTML container added keyboard support    
  - att-accessibility directive implemented

- **Accordion**
  - Arrow navigation added for child items in Accordion

- **Dropdown**
  - Item highlight conflict with Keyboard and Mouse hover fixed

- **Datepicker**
  - Updated aria-hidden attribute for accessibility.

- **attCheckbox**
  - Added id attribute that will be wrapped at parent DIV

## Breaking Changes




# 1.2.45

## Features

- **Month Filter**
  - Added calendar icon in month filter.
  - Updated functionality to return current selected string, with from and current month.

- **Pagination**
  - Added click handler

- **Scrolling table demo**
  - Added demo on how to do scrolling table for attTable component

## Bug Fixes

- **Tooltips**
  - Tooltips now float on top of modal.

- **Split Icon Button**
  - Fix NVDA screenreader issue for split icon button.
  - Icon dropdowns now close on mouse click
  - Fixed tab selection issue for split icon buttons in Firefox
  - Tabbing no longer selects the icons in the buttons in IE8

- **TagBadges**
  - Accessibility issues resolved.

- **Accordion Multipurpose Expander**
  - Expand/Collapse through Keyboard fixed
  - Customer HTML container added keyboard support

- **Text Dropdown**
  - Event handler now fires for the selected event

- **Att Step Slider**
  -Add support for updating while dragging

## Breaking Changes




# 1.2.44

## Features



## Bug Fixes

- **Dropdown**
  - Keyboard navigation issue fixed for IE8

- **Datepicker**
  - Updated monthpicker's look and feel.

- **type Ahead**
  - Added scroll on input, css fixes.

- **Dropdown**
  - Type Ahead Search will not Include Function Keys, Special Keys, Control Keys, Space & Backspace.     
  - Dropdown will be scrolled to show selected Item as first item in the list.
  - In case of Dropdown with scrollbar, keyboard navigation will move the list up & down if needed.
  - Backspace will reset the Type ahead filter.
  - Selecting same Item with Enter key bug fixed

## Breaking Changes




# 1.2.43

## Features



## Bug Fixes

- **Table**
  - Fixed sort ordering issue in IE8.

## Breaking Changes




# 1.2.42

## Features

- **Datepicker**
  - Added monthpicker functionality.

## Bug Fixes

- **Datepicker**
  - Updated accessibility.
  - Accept valid dates with trimmed zero.

- **Utilities**
  - Added lPad and rPad function to javascript String object for padding.

## Breaking Changes




# 1.2.41

## Features



## Bug Fixes

- **HourPicker**
  - Dropdown overflow issue fixed
  - Auto-scroll failing scenarios with cross-browser compatibility issues.

## Breaking Changes




# 1.2.40

## Features

- **HourPicker**
  - Selected item should be highlighted.
  - Added functionality for auto-scroll to selected item

## Bug Fixes

- **HourPicker**
  - Width fix for dropdown items 

## Breaking Changes




# 1.2.38

## Features

- **HourPicker**
  - Added dropdown arrow 

## Bug Fixes

- **StepTracker**
  - Labels should respect the case they are given.
  - Labels are now left aligned to the step's circle.

- **utilities**
  - Added validation in `attLimitTo` filter.

- **Datepicker**
  - Updated `datepickerService` and removed `disable-dates` because of issues.

- **Dropdown**
  - Fixed automatically redirecting on opening of dropdown.

## Breaking Changes




# 1.2.37

## Features



## Bug Fixes

- **StepTracker**
  - Fixed steps looking like ovals instead of circles
  - Fixed wrong highlight color on active steps
  - Added not-allowed cursor for disabled steps

- **hourpicker**
  - Bugfix for reverting to default values for unchecked boxes

  - **Split Icon Button**
    - Fix CATO issues
    
  - **TextDropdown**
    - Updated for CATO Compliance.

- **Split Icon Button**
  - Fix CATO issues

- **Datepicker**
  - Updated disable previous and disable next logic.
  - Updated date validation logic.
  - Implemented preserving the last shown month on entering invalid date.

## Breaking Changes




# 1.2.36

## Features

- **StepTracker**
  - Added disable-click attribute for ability to disable all click events for steps.

- **utilities**
  - Added new filter `attLimitTo`.

## Bug Fixes

- **Buttons**
  - Fix on disabled button click issue.
  - Directive code optimization.

- **Datepicker**
  - Date string validation for disabled dates.
  - Update disable previous and disable next issue.

- **attMessage**
  - Assign it to be a child scope, so that multiple `att-message` can be used in a single page.

- **utilities**
  - Added few validation in `att-input-deny`.
  
- **StepTracker**
  - Fixed size of steps.
  - Fixed roundness of last step.

- **Table**
  - Implemented `attLimitTo` filter for the implementation of views per page, instead of `ng-show`.

- **Toggle**
 - Fixed accessibility issue

- **attStepSlider**
  - scale is now in real values.
  - fixed bugs with cuttoff point failing.

- **attTimeline**
  - updated colors for completed and cancelled steps.

## Breaking Changes




# 1.2.34

## Features



## Bug Fixes

- **tagBadges**
  - tag with custom color now dynamically changing color.
  - tag with custom color background color issue fixed.

- **Hour Picker**
  - on change of model value in hour picker component, same value is not updating in hour picker component.

## Breaking Changes




# 1.2.33

## Features

- **megaMenu**
  - The swapping functionality for menus is added.

- **Checkbox**
  - checkbox should show title based on value provided.

- **Radio Button**
  - radio button should show title based on value provided.

- **att timeline**
  - timeline component.  
  
## Bug Fixes



## Breaking Changes
