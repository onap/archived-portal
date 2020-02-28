import { TestBed } from '@angular/core/testing';

import { ManifestService } from './manifest.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ManifestService', () => {
  beforeEach(() => TestBed.configureTestingModule({imports:[HttpClientTestingModule]}));

  it('should be created', () => {
    const service: ManifestService = TestBed.get(ManifestService);
    expect(service).toBeTruthy();
  });
});
