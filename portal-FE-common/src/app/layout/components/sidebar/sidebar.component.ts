/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright © 2019 AT&T Intellectual Property. All rights reserved.
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
import { Component, Output, EventEmitter, OnInit, Input } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { SidebarService } from '../../../shared/services/index'

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
    @Input() labelName: string;
    isActive: boolean;
    collapsed: boolean;
    showMenu: string;
    pushRightClass: string;
    result: any;
    showOnlyParentMenu: boolean;
    leftParentData: any;
    leftChildData: any;
    menuData: Array<object> = [];
    page: any;

    @Output() collapsedEvent = new EventEmitter<boolean>();

    constructor(public router: Router, public sidebarService: SidebarService) {
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
        this.isActive = false;
        this.collapsed = false;
        this.showMenu = '';
        this.pushRightClass = 'push-right';
        this.sidebarService.getLeftMenu()
            .subscribe(data => {
                this.result = data;
                if (this.result.data && this.result.data2) {
                    this.leftParentData = JSON.parse(this.result.data);
                    this.leftChildData = JSON.parse(this.result.data2);
                } else {
                    this.labelName = this.result.label;
                    this.leftParentData = this.result.navItems;
                    this.showOnlyParentMenu = true;
                }

                for (var i = 0; i < this.leftParentData.length; i++) {
                    var parentItem = {
                        name: null,
                        imageSrc: null,
                        href: null,
                        menuItems: [],
                        state: null
                    }
                    if (this.showOnlyParentMenu) {
                        parentItem.name = this.leftParentData[i].name;
                        parentItem.imageSrc = this.leftParentData[i].imageSrc;
                        parentItem.state = '/'+this.leftParentData[i].state;
                    } else {
                        parentItem.name = this.leftParentData[i].label;
                        parentItem.imageSrc = this.leftParentData[i].imageSrc;
                    }
                    // Add link to items with no subitems
                    if (!this.showOnlyParentMenu) {
                        if (this.leftChildData[i].length == 0)
                            parentItem.href = this.leftParentData[i].action;

                        for (var j = 0; j < this.leftChildData[i].length; j++) {

                            var childItem = {
                                name: null,
                                href: null
                            };
                            if (this.leftChildData[i][j].label != null && this.leftChildData[i][j].label.length > 0) {

                                childItem.name = this.leftChildData[i][j].label;
                                childItem.href = this.leftChildData[i][j].action;
                                parentItem.menuItems.push(childItem);
                            }
                        }
                    }
                    this.menuData.push(parentItem);
                }

            });

    }
    eventCalled() {
        this.isActive = !this.isActive;
    }

    addExpandClass(element: any) {
        if (element === this.showMenu) {
            this.showMenu = '0';
        } else {
            this.showMenu = element;
        }
    }

    toggleCollapsed() {
        this.collapsed = !this.collapsed;
        this.collapsedEvent.emit(this.collapsed);
    }

    isToggled(): boolean {
        const dom: Element = document.querySelector('body');
        return dom.classList.contains(this.pushRightClass);
    }

    toggleSidebar() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle(this.pushRightClass);
    }
}
