import { TestBed } from '@angular/core/testing';

import { AuditLogService } from './audit-log.service';

describe('AuditLogService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AuditLogService = TestBed.get(AuditLogService);
    expect(service).toBeTruthy();
  });
});
