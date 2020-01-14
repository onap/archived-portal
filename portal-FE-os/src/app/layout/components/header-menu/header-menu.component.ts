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
import * as _ from 'underscore';
import { Component, OnInit } from '@angular/core';
import { MenusService } from 'src/app/shared/services/menus/menus.service';
import { AddTabFunctionService } from 'src/app/shared/services/tab/add-tab-function.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-menu',
  templateUrl: './header-menu.component.html',
  styleUrls: ['./header-menu.component.scss']
})
export class HeaderMenuComponent implements OnInit {
  hideMenus: boolean[] = [];
  hideSecondLevelMenus: boolean[][] = [];
  hideThirdLevelMenus: boolean[] = [];
  megaMenuDataObject: any[];
  favoritesMenuItems: any[];
  favoritesWindow: boolean;
  showFavorites: boolean;
  emptyFavorites: boolean;
  favoriteItemsCount: number;

  constructor(public router: Router, private menusService: MenusService, private addTabFuntionService: AddTabFunctionService) { }

  ngOnInit() {
    //this.hideMenus[0] = false;
    this.getFunctionalMenuForUser();

  }

  unflatten(array: any, parent?: any, tree?: any) {
    tree = typeof tree !== 'undefined' ? tree : [];
    parent = typeof parent !== 'undefined' ? parent : { menuId: null };
    var children = _.filter(array, function (child: any) {
      return child.parentMenuId == parent.menuId;
    });
    if (!_.isEmpty(children)) {
      if (parent.menuId === null) {
        tree = children;
      } else {
        parent['children'] = children
      }
      _.each(children, function (child: any) {
        this.unflatten(array, child)
      }, this);
    }

    return tree;
  }
  getFunctionalMenuForUser() {
    this.menusService.getFunctionalMenuForUser().subscribe((jsonHeaderMenu: any) => {
      this.megaMenuDataObject = this.unflatten(jsonHeaderMenu);
      // for (let entry of this.megaMenuDataObject) {
      //   console.log('First level '+entry.text);
      //   for (let secondLevel of entry.children)
      //   {
      //     if(secondLevel)
      //   {
      //     console.log('Second level '+secondLevel.text);
      //     for (let thirdLevel of secondLevel.children)
      //   {
      //     console.log('Third level '+thirdLevel.text);
      //   }
      //   }

      //   }

      // }


    }, (err) => {
      console.log('HeaderCtrl::GetFunctionalMenuForUser: HeaderCtrl json returned: ' + err);
    });

  }
  getFavoriteItems() {
    this.menusService.getFavoriteItems().toPromise().then((jsonFavourites: any) => {
      this.favoritesMenuItems = jsonFavourites;
      if (this.favoritesMenuItems) {
        this.favoriteItemsCount = this.favoritesMenuItems.length;
      }
    }, (err) => {
      console.log('HeaderCtrl::getFavoriteItems: HeaderCtrl json returned: ' + err);
    });
  }
  loadFirstLevel(index: any) {
    this.hideMenus = [];
    this.hideSecondLevelMenus = [];
    for (let firstLevelIndex = 0; firstLevelIndex < this.megaMenuDataObject.length; firstLevelIndex++) {
      this.hideMenus.push(false);
      this.hideSecondLevelMenus.push(new Array(this.megaMenuDataObject[firstLevelIndex].length).fill(false));
    }
    this.hideMenus[index] = true;
    if (!this.favoritesMenuItems) {
      this.getFavoriteItems();
    }

  }
  isUrlFavorite(menuId: any) {
    if (this.favoritesMenuItems) {
      var jsonMenu = JSON.stringify(this.favoritesMenuItems);
      var isMenuFavorite = jsonMenu.indexOf('menuId\":' + menuId);
      if (isMenuFavorite == -1) {
        return false;
      } else {
        return true;
      }
    }
    else {
      return false;
    }
  }
  submenuLevelAction(index: any, column: any) {
    //console.log('index and column' + index + column);
    if (index == 'Favorites' && this.favoriteItemsCount != 0) {
      this.favoritesWindow = true;
      this.showFavorites = true;
      this.emptyFavorites = false;
    }
    if (index == 'Favorites' && this.favoriteItemsCount == 0) {
      this.favoritesWindow = true;
      this.showFavorites = false;
      this.emptyFavorites = true;
    }
    if (index != 'Favorites') {
      this.favoritesWindow = false;
      this.showFavorites = false;
      this.emptyFavorites = false;
    }
  }
  hideFavoritesWindow() {
    this.showFavorites = false;
    this.emptyFavorites = false;
  }
  removeAsFavoriteItem(event: any, menuId: any) {
    this.menusService.removeFavoriteItem(menuId).subscribe(() => {
      //angular.element('#' + event.target.id).css('color', '#666666');
      this.getFavoriteItems();
    }, (err) => {
      console.error('HeaderCtrl::removeAsFavoriteItem: API removeFavoriteItem error: ' + err);
    });
  }
  hideThirdLevelMenu(firstLevelIndex: any, secondLevelIndex: any) {
    this.hideSecondLevelMenus = [];
    for (let firstLevelIndex = 0; firstLevelIndex < this.megaMenuDataObject.length; firstLevelIndex++) {
      this.hideSecondLevelMenus.push(new Array(this.megaMenuDataObject[firstLevelIndex].length).fill(false));
    }
    this.hideSecondLevelMenus[firstLevelIndex][secondLevelIndex] = true;
  }

  clickOutSide(event: any) {
    this.hideMenus = [];
    this.hideSecondLevelMenus = [];
    for (let firstLevelIndex = 0; firstLevelIndex < this.megaMenuDataObject.length; firstLevelIndex++) {
      this.hideMenus.push(false);
      this.hideSecondLevelMenus.push(new Array(this.megaMenuDataObject[firstLevelIndex].length).fill(false));
    }

  }
  setAsFavoriteItem(event: any, menuId: any) {

  }
  goToUrl(item: any) {
    //console.log('Get into URL function' + item.url);
    let url = item.url;
    let restrictedApp = item.restrictedApp;
    if (!url) {
      console.log('HeaderCtrl::goToUrl: No url found for this application, doing nothing..');
      return;
    }
    if (restrictedApp) {
      window.open(url, '_blank');
    } else {
      if (item.url == "getAccess" || item.url == "contactUs") {

        this.router.navigate(['/' + item.url]);

      } else {
        var tabContent = {
          id: new Date(),
          title: item.text,
          url: item.url,
          appId: item.appid
        };
        this.addTabFuntionService.filter(tabContent);
      }
    }

  }
  auditLog(link: any, action: any) {

  }

}
