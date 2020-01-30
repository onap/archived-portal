import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetAccessComponent } from './get-access.component';

describe('GetAccessComponent', () => {
  let component: GetAccessComponent;
  let fixture: ComponentFixture<GetAccessComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetAccessComponent ]
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
