import { TestBed } from '@angular/core/testing';

import { UserbarService } from './userbar.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('UserbarService', () => {
  beforeEach(() => TestBed.configureTestingModule({imports:[HttpClientTestingModule]}));

  it('should be created', () => {
    const service: UserbarService = TestBed.get(UserbarService);
    expect(service).toBeTruthy();
  });
});
