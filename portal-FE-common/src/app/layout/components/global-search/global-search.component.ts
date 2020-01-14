/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { GlobalSearchService } from 'src/app/shared/services/global-search/global-search.service';
import { GlobalSearchItem } from 'src/app/shared/model/global-search-item.model';
import * as $ from 'jquery';
import { AddTabFunctionService } from 'src/app/shared/services/tab/add-tab-function.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-global-search',
  templateUrl: './global-search.component.html',
  styleUrls: ['./global-search.component.scss']
})
export class GlobalSearchComponent implements OnInit {
  searchResDialog: boolean = false;
  items: any;
  searchString: string;
  constructor(private globalSearchService: GlobalSearchService, private addTabFuntionService: AddTabFunctionService, private router: Router) { }

  ngOnInit() {
  }

  showHideSearchSnippet() {
    setTimeout(() => {
      $('#mainSearchSnippet').click();
    }, 1000);
    setTimeout(() => {
      $('mainSearchText').focus();
    }, 1000);
  }

  mainSearchEvent = $('#mainSearchDiv').keyup((event) => {
    if (event.keyCode == 13) {

      this.getSearchResult(<string><any>$('#mainSearchText').val());

      // opens the popup
      var popupDomObj = $("[content='searchSnippet.html']");
      if (popupDomObj.length == 0) {
        this.showHideSearchSnippet();
      } else {
        $('#mainSearchSnippet').click();
        this.showHideSearchSnippet();
      }



    }
  });

  clickOutSide(event: any) {

    this.searchResDialog = false;

  }
  searchDialogToggle(event: any) {
    if (event.keyCode == 13) {
      this.searchResDialog = true;
      this.searchString = <string><any>$('#mainSearchText').val();
      this.getSearchResult(<string><any>$('#mainSearchText').val());
    }
  }

  getSearchResult(searchString: string) {
    //console.log("getSearch Result");
    this.globalSearchService.getSearchResults(searchString).subscribe(data => {
      //console.log("Response data" + data);
      this.items = data.response;
      //console.log("search result data" + JSON.stringify(data));

    }, error => {
      console.log('getSearchResult Error Object' + error);
    });
  };
  goToUrl(item: any, type?: any) {
    //console.log('check goto');
    //var a = { 'test1': 'value1', 'test2': 'value3', 'test3': 'value2' };
    if (type == 'intra') {

      var intraSearcLink = "http://insider.web.att.com/s/s.dll?spage=search%2FVeritySearchResult.htm&QueryText=";
      var intraSpecSearcLink = intraSearcLink + encodeURIComponent(this.searchString);
      window.open(intraSpecSearcLink, '_blank');

    } else if (type == 'extra') {
      var extraSearcLink = "https://www.att.com/global-search/search.jsp?q=";
      var extraSpecSearcLink = extraSearcLink + encodeURIComponent(this.searchString);
      window.open(extraSpecSearcLink, '_blank');
    }
    let url = item.target;
    let restrictedApp = item.uuid;
    let getAccessState = "getAccess"
    console.log("item.target " + item.target + "item.uuid " + item.uuid);
    if (!url) {
      this.router.navigate(['/' + getAccessState]);
      //$log.info('No url found for this application, doing nothing..');
      return;
    }
    if (!restrictedApp) {
      window.open(url, '_blank');
    } else {
      if (item.url == "root.access") {
        this.router.navigate(['/' + item.url]);
        var tabContent = { id: new Date(), title: 'Home', url: url };
        // $cookies.putObject('addTab', tabContent );
        this.addTabFuntionService.filter(tabContent);
      } else {
        var tabContentCtrl = { id: new Date(), title: item.name, url: url };
        this.addTabFuntionService.filter(tabContentCtrl);
      }
    }

  }

}
