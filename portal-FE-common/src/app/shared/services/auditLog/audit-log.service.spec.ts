import { TestBed } from '@angular/core/testing';

import { AuditLogService } from './audit-log.service';
import { HttpClientTestingModule} from '@angular/common/http/testing';

describe('AuditLogService', () => {
  beforeEach(() => TestBed.configureTestingModule({imports: [HttpClientTestingModule]}));

  it('should be created', () => {
    const service: AuditLogService = TestBed.get(AuditLogService);
    expect(service).toBeTruthy();
  });
});
