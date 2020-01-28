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
import { Component, OnInit } from '@angular/core';
import { UserbarService, UserProfileService } from 'src/app/shared/services';
import { DomSanitizer } from '@angular/platform-browser';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-userbar',
  templateUrl: './userbar.component.html',
  styleUrls: ['./userbar.component.scss']
})
export class UserbarComponent implements OnInit {

  userList;
  isOpen: boolean;
  intervalPromise = null;
  updateRate: number;
  myservice: UserbarService;
  api = environment.api;
  constructor(private sanitizer: DomSanitizer, private userbarService: UserbarService, private userProfileService: UserProfileService) { }

  ngOnInit() {
    this.userList = [];
    this.myservice = this.userbarService;
    this.isOpen = true;
    // this.userbarService.getOnlineUserUpdateRate().subscribe((_res: any) => {
    //   if (_res != null) {
    //     var rate = parseInt(_res.onlineUserUpdateRate);
    //     var duration = parseInt(_res.onlineUserUpdateDuration);
    //     this.userbarService.setMaxRefreshCount((duration / rate) + 1);
    //     this.userbarService.setRefreshCount(this.userbarService.maxCount);
    //     if (rate != NaN && duration != NaN) {
    //       // $log.debug('UserbarCtlr: scheduling function at interval ' + millis);
    //       this.updateRate = rate;
    //       this.start(this.updateRate);
    //     }
    //   }
    // })
    this.updateActiveUsers();
  }

  updateActiveUsers() {
    // this.userbarService.decrementRefreshCount();
    this.userProfileService.getActiveUser().subscribe((_res: any) => {
      if (_res == null) {
        // $log.error('UserbarCtrl::updateActiveUsers: failed to get active user');
        this.stop();
      } else {
        var maxItems = 25;
        if (_res.length < maxItems)
          maxItems = _res.length;
        for (var i = 0; i < maxItems; i++) {
          var data = {
            userId: _res[i],
            linkQ: this.api.linkQ,
            linkPic: this.api.linkPic
          }
          this.userList.push(data);
        }
      }

    }, (err) => {
      this.userList = [];
      this.stop();
    })

    // .add(() => {
    //   var footerOff = $('#online-userbar').offset().top;
    //   var headOff = $('#footer').offset().top;
    //   var defaultOffSet = 45;
    //   $(".online-user-container").css({
    //     "height": headOff - footerOff - defaultOffSet
    //   });
    // })

  }

  toggleSidebar() {
    this.isOpen = !this.isOpen;
  }

  start(rate) {
    // stops any running interval to avoid two intervals running at the same time
    this.stop();
    // store the interval promise
    this.intervalPromise = setInterval(this.updateActiveUsers, rate);
  }


  stop() {
    if (this.intervalPromise != null) {
      clearInterval(this.intervalPromise);
      this.intervalPromise = null;
    }
  }

}
