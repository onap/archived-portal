import { TestBed } from '@angular/core/testing';

import { GetAccessService } from './get-access.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('GetAccessService', () => {
  beforeEach(() => TestBed.configureTestingModule({imports:[HttpClientTestingModule]}));

  it('should be created', () => {
    const service: GetAccessService = TestBed.get(GetAccessService);
    expect(service).toBeTruthy();
  });
});
