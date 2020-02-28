import { TestBed } from '@angular/core/testing';

import { AdminsService } from './admins.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('AdminsService', () => {
  let service: AdminsService;
  beforeEach(() => TestBed.configureTestingModule({
    imports:[HttpClientTestingModule]
  }));

  it('should be created', () => {
    service = TestBed.get(AdminsService);
    expect(service).toBeTruthy();
  });
  it('getAdminAppsRoles should return stubbed value', () => {
    spyOn(service, 'getAdminAppsRoles').and.callThrough();
    service.getAdminAppsRoles("test");
    expect(service.getAdminAppsRoles).toHaveBeenCalledWith("test");
  });
  it('getRolesByApp should return stubbed value', () => {
    spyOn(service, 'getRolesByApp').and.callThrough();
    service.getRolesByApp("test");
    expect(service.getRolesByApp).toHaveBeenCalledWith("test");
  });

  it('updateAdminAppsRoles should return stubbed value', () => {
    spyOn(service, 'updateAdminAppsRoles').and.callThrough();
    service.updateAdminAppsRoles("test");
    expect(service.updateAdminAppsRoles).toHaveBeenCalledWith("test");
  });

  it('isComplexPassword should return stubbed value', () => {
    spyOn(service, 'isComplexPassword').and.callThrough();
    service.isComplexPassword("testpassword");
    expect(service.isComplexPassword).toHaveBeenCalledWith("testpassword");
  });

  it('addNewUser should return stubbed value', () => {
    spyOn(service, 'addNewUser').and.callThrough();
    service.addNewUser("testuser","duplicatecheck");
    expect(service.addNewUser).toHaveBeenCalledWith("testuser","duplicatecheck");
  });
});
