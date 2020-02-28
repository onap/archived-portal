import { TestBed } from '@angular/core/testing';

import { UtilsService } from './utils.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('UtilsService', () => {
  beforeEach(() => TestBed.configureTestingModule({imports:[HttpClientTestingModule]}));

  it('should be created', () => {
    const service: UtilsService = TestBed.get(UtilsService);
    expect(service).toBeTruthy();
  });
});
