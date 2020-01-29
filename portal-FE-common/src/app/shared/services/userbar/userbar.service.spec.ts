import { TestBed } from '@angular/core/testing';

import { UserbarService } from './userbar.service';

describe('UserbarService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserbarService = TestBed.get(UserbarService);
    expect(service).toBeTruthy();
  });
});
