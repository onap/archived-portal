import { TestBed } from '@angular/core/testing';

import { PortalAdminsService } from './portal-admin.service';

describe('AdminsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PortalAdminsService = TestBed.get(PortalAdminsService);
    expect(service).toBeTruthy();
  });
});
