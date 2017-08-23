This file tracks patches to b2b-angular.css


14 March 2017: Replaced occurrences of font names in about 180 places:
Replace "ATT" with "ECOMP" in font-family in b2b-angular.css file
-  font-family: "Omnes-ATT-W02-Medium";
+  font-family: "Omnes-ECOMP-W02-Medium";
 
-  font-family: "Omnes-ATT-W02";
+  font-family: "Omnes-ECOMP-W02";

15 March 2017: Revert the change back to original for input:not([type="button"])
 - height: 14px; 
 + height: 36px;
 
22 June 2017: Fix the missing icons by doing the mapping with ionicons
Removed the "!important" tag for the "font family: icoPrimary"
 - font family: 'icoPrimary' !important;
 + font family: 'icoPrimary';