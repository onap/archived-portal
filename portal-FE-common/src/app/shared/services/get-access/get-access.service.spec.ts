import { TestBed } from '@angular/core/testing';

import { GetAccessService } from './get-access.service';

describe('GetAccessService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GetAccessService = TestBed.get(GetAccessService);
    expect(service).toBeTruthy();
  });
});
