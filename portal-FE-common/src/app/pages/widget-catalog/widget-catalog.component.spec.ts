import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WidgetCatalogComponent } from './widget-catalog.component';

describe('WidgetCatalogComponent', () => {
  let component: WidgetCatalogComponent;
  let fixture: ComponentFixture<WidgetCatalogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WidgetCatalogComponent ]
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
