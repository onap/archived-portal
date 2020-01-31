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
import { Injectable } from '@angular/core';
import { IWidgetCatalog } from '../../model/widget-catalog.model';
import { Observable, from } from 'rxjs';
import { GridsterConfig, GridsterItem, CompactType, DisplayGrid, GridType } from 'angular-gridster2';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';



@Injectable({
  providedIn: 'root'
})
export class WidgetCatalogService {
  public options: GridsterConfig = {
    minCols: 6,
    maxCols: 6,
    minRows: 7,
    //maxRows: 4,
    maxItemCols: 50,
    minItemCols: 1,
    maxItemRows: 50,
    minItemRows: 1,
    maxItemArea: 2500,
    minItemArea: 1,
    defaultItemCols: 2,
    defaultItemRows: 2,
    setGridSize: false,
    fixedColWidth: 250,
    fixedRowHeight: 250,
    gridType: GridType.ScrollVertical,
    swap: true,
    dynamicColumns: true,
    displayGrid: DisplayGrid.None,


    draggable: {
      enabled: true
    },
    pushItems: true,
    resizable: {
      enabled: true
    }
  };
  public layout: GridsterItem[] = [];
  constructor(private api: HttpClient) { }
  addItem(widgetData: any): void {
    this.layout.push(widgetData);
  }
  widgetCatalogData: IWidgetCatalog[] = [{
    widgetId: '1',
    widgetName: 'TestData1',
    widgetStatus: 'TestStatus1',
    imageLink: 'week_1.png',
    select: true
  },
  {
    widgetId: '2',
    widgetName: 'TestData1',
    widgetStatus: 'TestStatus1',
    imageLink: 'week_1.png',
    select: true
  },
  {
    widgetId: '3',
    widgetName: 'TestData1',
    widgetStatus: 'TestStatus1',
    imageLink: 'week_1.png',
    select: true
  },
  {
    widgetId: '4',
    widgetName: 'TestData1',
    widgetStatus: 'TestStatus1',
    imageLink: 'week_1.png',
    select: true
  },
  {
    widgetId: '5',
    widgetName: 'TestData1',
    widgetStatus: 'TestStatus1',
    imageLink: 'week_1.png',
    select: true
  },
  {
    widgetId: '6',
    widgetName: 'TestData1',
    widgetStatus: 'TestStatus1',
    imageLink: 'week_1.png',
    select: true
  },
  {
    widgetId: '7',
    widgetName: 'TestData1',
    widgetStatus: 'TestStatus1',
    imageLink: 'week_1.png',
    select: true
  },
  {
    widgetId: '8',
    widgetName: 'TestData1',
    widgetStatus: 'TestStatus1',
    imageLink: 'week_1.png',
    select: true
  }];


  public getWidgetCatalog(): any {
    const widgetCatalogObservable = new Observable(observer => {
      setTimeout(() => {
        observer.next(this.widgetCatalogData);
      }, 1000);
    });

    return widgetCatalogObservable;
  }
  getUserWidgets(loginName: string): Observable<any> {
    return this.api.get(environment.api.widgetCommon + '/widgetCatalog' + '/' + loginName);
  }
  getManagedWidgets(): Observable<any> {
    return this.api.get(environment.api.widgetCommon + '/widgetCatalog');
  }
  getUploadFlag(): Observable<any> {
    return this.api.get(environment.api.widgetCommon + '/uploadFlag');
  }
  // createWidget(newWidget: any, widgetNameparam: string): Observable<any> {
  //   return this.api.post(environment.api.widgetCommon + '/widgetCatalog', newWidget, widgetNameparam);
  // }

  // updateWidgetWithFile(newWidget: any, widgetNameparam: string, widgetIdParam: string): Observable<any> {
  //   return this.api.post(environment.api.widgetCommon + '/widgetCatalog/' + widgetIdParam, newWidget, widgetNameparam);
  // }

  updateWidget(newWidget: any, widgetIdParam: string): Observable<any> {
    return this.api.put(environment.api.widgetCommon + '/widgetCatalog/' + widgetIdParam, newWidget);
  }
  updateWidgetCatalog(appData: any): Observable<any> {
    const headers = new HttpHeaders().set('X-Widgets-Type','all');
    return this.api.put(environment.api.widgetCatalogSelection, appData,{headers});
  }
}
