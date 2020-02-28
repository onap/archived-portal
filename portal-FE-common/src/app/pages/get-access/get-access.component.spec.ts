import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetAccessComponent } from './get-access.component';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('GetAccessComponent', () => {
  let component: GetAccessComponent;
  let fixture: ComponentFixture<GetAccessComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetAccessComponent ],
      imports:[NgMaterialModule,HttpClientTestingModule,BrowserAnimationsModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetAccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
