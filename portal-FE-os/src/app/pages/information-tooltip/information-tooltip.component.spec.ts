import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InformationTooltipComponent } from './information-tooltip.component';

describe('InformationTooltipComponent', () => {
  let component: InformationTooltipComponent;
  let fixture: ComponentFixture<InformationTooltipComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InformationTooltipComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InformationTooltipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
