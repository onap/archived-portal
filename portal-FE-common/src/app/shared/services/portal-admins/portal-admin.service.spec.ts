import { TestBed } from '@angular/core/testing';

import { PortalAdminsService } from './portal-admin.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('AdminsService', () => {
  beforeEach(() => TestBed.configureTestingModule({imports:[HttpClientTestingModule]}));

  it('should be created', () => {
    const service: PortalAdminsService = TestBed.get(PortalAdminsService);
    expect(service).toBeTruthy();
  });
});
