/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
/**
 * Author: Rui Lu
 * 12/15/2016
 */
(function () {
    class Base64Service {
    	 constructor(){
    		
    	 }
    	 encode(input) {
    		 var keyStr = 'ABCDEFGHIJKLMNOP' +
             'QRSTUVWXYZabcdef' +
             'ghijklmnopqrstuv' +
             'wxyz0123456789+/' +
             '=';
    		 var output = "";
             var chr1, chr2, chr3 = "";
             var enc1, enc2, enc3, enc4 = "";
             var i = 0;

             do {
                 chr1 = input.charCodeAt(i++);
                 chr2 = input.charCodeAt(i++);
                 chr3 = input.charCodeAt(i++);

                 enc1 = chr1 >> 2;
                 enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                 enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                 enc4 = chr3 & 63;

                 if (isNaN(chr2)) {
                     enc3 = enc4 = 64;
                 } else if (isNaN(chr3)) {
                     enc4 = 64;
                 }

                 output = output +
                         keyStr.charAt(enc1) +
                         keyStr.charAt(enc2) +
                         keyStr.charAt(enc3) +
                         keyStr.charAt(enc4);
                 chr1 = chr2 = chr3 = "";
                 enc1 = enc2 = enc3 = enc4 = "";
             } while (i < input.length);

             return output;
    	 }
    }
    angular.module('ecompApp').service('base64Service', Base64Service)
})();
