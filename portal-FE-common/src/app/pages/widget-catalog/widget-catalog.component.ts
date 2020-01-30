import { Component, OnInit } from '@angular/core';
import { GridsterConfig, GridsterItem } from 'angular-gridster2';
import { WidgetCatalogService } from '../../shared/services/widget-catalog/widget-catalog.service';
import { IWidgetCatalog } from '../../shared/model/widget-catalog.model';
import { Observable } from 'rxjs';
import { inflateRaw } from 'zlib';
import { UsersService } from 'src/app/shared/services/users/users.service';


@Component({
  selector: 'app-widget-catalog',
  templateUrl: './widget-catalog.component.html',
  styleUrls: ['./widget-catalog.component.scss']
})
export class WidgetCatalogComponent implements OnInit {
  widgetCatalogData: IWidgetCatalog[];
  isCommonError: boolean = false;
  isApiRunning: boolean = true;
  userName: string;


  get options(): GridsterConfig {
    return this.widgetCatalogService.options;
  } get layout(): GridsterItem[] {
    return this.widgetCatalogService.layout;
  } constructor(private widgetCatalogService: WidgetCatalogService, private userService: UsersService) {

  }

  ngOnInit() {
    const widgetCatalogObservable = this.widgetCatalogService.getWidgetCatalog();
    this.widgetCatalogService.layout = [];
    this.getUserWidgets(this.userName);
  }


  getUserWidgets(loginName: string) {
    const widgetCatalogUserObservable = this.userService.getUserProfile();
    widgetCatalogUserObservable.subscribe((userProfile: any) => {
      //console.log('UserProfile is ' + userProfile);
      if (userProfile) {
        const widgetCatalogObservable = this.widgetCatalogService.getUserWidgets(userProfile.orgUserId);
        widgetCatalogObservable.subscribe(data => {
          //console.log("What is coming from backend" + JSON.parse(data));
          this.widgetCatalogData = data;
          console.log(this.widgetCatalogData);
          for (let entry of this.widgetCatalogData) {
            if (entry[1] == 'Events' || entry[1] == 'News' || entry[1] == 'Resources') {
              var appCatalog = {
                id: entry[0],
                name: entry[1],
                headerName: entry[2],
                select: (entry[4] == 'S' || entry[4] === null) ? true : false
              };
              this.widgetCatalogService.addItem(appCatalog);
            }
          }
        });
      }
    });

  }

  getUserProfile(): Observable<any> {
    const widgetCatalogObservable = this.userService.getUserProfile();
    return widgetCatalogObservable;
  }

  storeSelection(widgetCatalogData: any) {
    console.log("Store selection called " + widgetCatalogData.select);
    if (widgetCatalogData) {
      var appData = {
        widgetId: widgetCatalogData.id,
        select: widgetCatalogData.select,
        pending:false
      };
      this.widgetCatalogService.updateWidgetCatalog(appData).subscribe(data => {
        //console.log("Update App sort data" + data);
      }, error => {
        console.log('updateWidgetCatalog error' + error);
      });
    }

  }
}