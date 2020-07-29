/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { FormControl } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Tab } from './tab';
import { AddTabFunctionService } from 'src/app/shared/services/tab/add-tab-function.service';

@Component({
  selector: 'app-tabbar',
  templateUrl: './tabbar.component.html',
  styleUrls: ['./tabbar.component.scss']
})
export class TabbarComponent implements OnInit {

  tabs = [];
  mainTab = 'Home';
  selected = new FormControl(0);
  collapedSideBar: boolean;
  inputedLanguage: string;
  @Input() language:string;

  constructor(private sanitizer: DomSanitizer, private addTabFuntionService: AddTabFunctionService) {

  }

  ngOnInit(): void {

    this.addTabFuntionService.listen().subscribe((m: any) => {
      console.log(m);
      this.addTab(true, m.title, m.url);
    })
  }

  ngOnChanges() {
    this.changeLang(this.language);
  }
  changeLang(lang){
    this.inputedLanguage=lang;
  }

  addTab(selectAfterAdding: boolean, label: string, url: string) {
    const tab = new Tab(label);
    tab.url = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    tab.active = true;
    this.tabs.push(tab);

    if (selectAfterAdding) {
      this.selected.setValue(this.tabs.length);
    }
  }

  removeTab(index: number) {
    this.tabs.splice(index, 1);
  }

  receiveCollapsed($event) {
    this.collapedSideBar = $event;
  }

  tabChanged($event) {

    for (const ttab of this.tabs) {
      ttab.active = false;
    }
    if(this.tabs.length != 0 && $event.index != 0)
      this.tabs[$event.index - 1].active = true;
  }
}
