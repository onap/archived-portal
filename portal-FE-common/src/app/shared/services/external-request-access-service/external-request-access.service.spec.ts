import { TestBed } from '@angular/core/testing';

import { ExternalRequestAccessService } from './external-request-access.service';

describe('ExternalRequestAccessService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ExternalRequestAccessService = TestBed.get(ExternalRequestAccessService);
    expect(service).toBeTruthy();
  });
});
