import { TestBed } from '@angular/core/testing';

import { ExternalRequestAccessService } from './external-request-access.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ExternalRequestAccessService', () => {
  beforeEach(() => TestBed.configureTestingModule({imports:[HttpClientTestingModule]}));

  it('should be created', () => {
    const service: ExternalRequestAccessService = TestBed.get(ExternalRequestAccessService);
    expect(service).toBeTruthy();
  });
});
