import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserbarComponent } from './userbar.component';

describe('UserbarComponent', () => {
  let component: UserbarComponent;
  let fixture: ComponentFixture<UserbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserbarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
