import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FunctionalMenuComponent } from './functional-menu.component';

describe('FunctionalMenuComponent', () => {
  let component: FunctionalMenuComponent;
  let fixture: ComponentFixture<FunctionalMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FunctionalMenuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FunctionalMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
