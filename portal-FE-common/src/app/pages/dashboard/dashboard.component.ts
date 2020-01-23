import { Component, OnInit } from '@angular/core';
import { UserProfileService } from 'src/app/shared/services';
import { PluginComponent} from 'src/app/shared/plugin/plugin.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  

  constructor(private userProfile: UserProfileService) { }

  ngOnInit() {
    // remove this after portal upgrade is done
    this.userProfile.getFunctionalMenuStaticInfo();
  }

  

}
