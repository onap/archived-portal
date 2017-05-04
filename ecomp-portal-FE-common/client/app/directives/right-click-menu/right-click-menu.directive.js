/*-
 * ================================================================================
 * eCOMP Portal
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
'use strict';
angular.module('ecompApp').directive( 'contextMenu', function($compile){
    contextMenu = {};
    contextMenu.restrict = 'AE';
    contextMenu.link = function( lScope, lElem, lAttr ){
        lElem.on('contextmenu', function (e) {
            e.preventDefault(); // default context menu is disabled
            //  The customized context menu is defined in the main controller. To function the ng-click functions the, contextmenu HTML should be compiled.
            lElem.append( $compile( lScope[ lAttr.contextMenu ])(lScope) );
            // The location of the context menu is defined on the click position and the click position is catched by the right click event.
            $('#contextmenu-node').css('left', e.clientX);
            $('#contextmenu-node').css('top', e.clientY);
        });
        lElem.on('mouseleave', function(e){
            console.log('Leaved the div');
            // on mouse leave, the context menu is removed.
            if($('#contextmenu-node') )
                $('#contextmenu-node').remove();
        });
    };
    return contextMenu;
});