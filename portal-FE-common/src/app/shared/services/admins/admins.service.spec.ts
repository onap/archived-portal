import { TestBed } from '@angular/core/testing';

import { AdminsService } from './admins.service';

describe('AdminsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AdminsService = TestBed.get(AdminsService);
    expect(service).toBeTruthy();
  });
});
