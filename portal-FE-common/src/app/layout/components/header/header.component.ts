/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright � 2019 AT&T Intellectual Property. All rights reserved.
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
import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { UserProfileService, MenusService } from 'src/app/shared/services';
import { CookieService } from 'ngx-cookie-service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
    public pushRightClass: string;
    firstName: string;
    lastName: string;
    loginSnippetUserid: any;
    lastLogin: number;
    loginSnippetEmail: any;
    userapproles: any[];
    displayUserAppRoles: any;
    isLoading: boolean;

    constructor(public router: Router, private userProfileService: UserProfileService, private menusService: MenusService, private cookieService: CookieService) {

        this.router.events.subscribe(val => {
            if (
                val instanceof NavigationEnd &&
                window.innerWidth <= 992 &&
                this.isToggled()
            ) {
                this.toggleSidebar();
            }
        });
    }

    ngOnInit() {
        this.pushRightClass = 'push-right';
        this.getUserInformation();
    }

    getUserInformation() {
        this.userProfileService.getFunctionalMenuStaticInfo().toPromise().then((res: any) => {
            if (res == null || res.firstName == null || res.firstName == '' || res.lastName == null || res.lastName == '') {
                // $log.info('HeaderCtrl: failed to get all required data, trying user profile');
                this.userProfileService.getUserProfile().toPromise().then((profile: any) => {
                    this.firstName = profile.firstName;
                    this.lastName = profile.lastName;
                }, (err) => {
                    // $log.error('Header Controller:: getUserProfile() failed: ' + err);
                });
            } else {
                this.firstName = res.firstName;
                this.lastName = res.lastName;
                this.loginSnippetEmail = res.email;
                this.loginSnippetUserid = res.userId;
                this.lastLogin = Date.parse(res.last_login);
            }
            sessionStorage.userId = res.userId;
            this.menusService.getFunctionalMenuForUser().toPromise().then((jsonHeaderMenu: any) => {
                // $scope.menuItems = unflatten(jsonHeaderMenu);
                // $scope.megaMenuDataObject = $scope.menuItems;
            }, (err) => {
                // $log.error('HeaderCtrl::GetFunctionalMenuForUser: HeaderCtrl json returned: ' + err);
            });

        }, (err) => {
            // $log.error('HeaderCtrl::getFunctionalMenuStaticInfo failed: ' + err);
        })
    }

    //     unflatten = function( array, parent, tree ){

    //     tree = typeof tree !== 'undefined' ? tree : [];
    //     parent = typeof parent !== 'undefined' ? parent : { menuId: null };
    //     var children = _.filter( array, function(child){ return child.parentMenuId == parent.menuId; });

    //     if( !_.isEmpty( children )  ){
    //       if( parent.menuId === null ){
    //         tree = children;
    //       }else{
    //         parent['children'] = children
    //       }
    //       _.each( children, function( child ){ unflatten( array, child ) } );
    //     }

    //     return tree;
    // }

    getUserApplicationRoles() {
        this.userapproles = [];
        if (this.displayUserAppRoles) {
            this.displayUserAppRoles = false;
        } else {
            this.displayUserAppRoles = true;
            this.isLoading = true;
            this.userProfileService.getUserAppRoles(this.loginSnippetUserid)
            .subscribe((res: any) => {
                this.isLoading = false;
                for (var i = 0; i < res.length; i++) {
                    var userapprole = {
                        App: res[i].appName,
                        Roles: res[i].roleNames,
                    };
                    this.userapproles.push(userapprole);
                }
            }, (err) => {
                this.isLoading = false;
            });
        }
    }

    allAppsLogout() {
        this.firstName="";
        this.lastName="";
        this.displayUserAppRoles=false; 	
        var cookieTabs = this.cookieService.get("visInVisCookieTabs").toString;
         if(cookieTabs!=null){
             for(var t in cookieTabs){
             
                 var url = cookieTabs[t].content;
                 if(url != "") {
                     this.menusService.logout(url);
                   }
             }
         }
         // wait for individual applications to log out before the portal logout
         setTimeout(function() {
             window.location.href = "logout.htm";
         }, 2000);
    }

    isToggled(): boolean {
        const dom: Element = document.querySelector('body');
        return dom.classList.contains(this.pushRightClass);
    }

    toggleSidebar() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle(this.pushRightClass);
    }

    onLoggedout() {
        localStorage.removeItem('isLoggedin');
    }
}
