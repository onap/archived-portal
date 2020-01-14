import { Component, OnInit } from '@angular/core';
import { UserbarService, UserProfileService } from 'src/app/shared/services';
import { DomSanitizer } from '@angular/platform-browser';

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
            linkQ: this.sanitizer.bypassSecurityTrustResourceUrl('qto://talk/' + _res[i]),
            linkPic: 'https://tspace.web.att.com/profiles/photo.do?uid=' + _res[i]
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
  };


  stop() {
    if (this.intervalPromise != null) {
      clearInterval(this.intervalPromise);
      this.intervalPromise = null;
    }
  };



}
