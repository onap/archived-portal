import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WidgetCatalogComponent } from './widget-catalog.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GridsterModule } from 'angular-gridster2';
import { ElipsisPipe } from 'src/app/shared/pipes/elipsis/elipsis.pipe';
import { Component, Input } from '@angular/core';

describe('WidgetCatalogComponent', () => {
  let component: WidgetCatalogComponent;
  let fixture: ComponentFixture<WidgetCatalogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WidgetCatalogComponent ,ElipsisPipe,AppDynamicWidgetStubComponent],
      imports:[HttpClientTestingModule,FormsModule,NgMaterialModule,BrowserAnimationsModule,GridsterModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WidgetCatalogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

@Component({selector: 'app-dynamic-widget', template: ''})
class AppDynamicWidgetStubComponent { 
  @Input() widgetType:any;
}
