import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminsService {

  constructor(private httpClient: HttpClient) { }
  apiUrl = environment.api;

  getAccountAdmins(): Observable<any> {
    return this.httpClient.get(this.apiUrl.accountAdmins);
  };

  getAdminAppsRoles(orgUserId: string): Observable<any> {
    let params = new HttpParams().set('user', orgUserId);
    return this.httpClient.get(this.apiUrl.adminAppsRoles, { params: params });
  };


  getRolesByApp(_appId: any): Observable<any> {
    return this.httpClient.get(this.apiUrl.adminAppsRoles + '/' + _appId);
  };

  updateAdminAppsRoles(_newAdminAppRoles: any): Observable<any> {
    return this.httpClient.put(this.apiUrl.adminAppsRoles, _newAdminAppRoles);
  };


  isComplexPassword(str) {
    let minLength = 8;
    let message = 'Password is too simple.  Minimum length is ' + minLength + ', '
      + 'and it must use letters, digits and special characters.';
    if (str == null)
      return message;

    let hasLetter = false;
    let hasDigit = false;
    let hasSpecial = false;
    var code, i, len;
    for (i = 0, len = str.length; i < len; i++) {
      code = str.charCodeAt(i);
      if (code > 47 && code < 58) // numeric (0-9)
        hasDigit = true;
      else if ((code > 64 && code < 91) || (code > 96 && code < 123)) // A-Z, a-z
        hasLetter = true;
      else
        hasSpecial = true;
    } // for

    if (str.length < minLength || !hasLetter || !hasDigit || !hasSpecial)
      return message;

    // All is well.
    return null;

  };

  addNewUser(newUser, checkDuplicate): Observable<any> {
    return this.httpClient.post(this.apiUrl.saveNewUser + '?isCheck=' + checkDuplicate, newUser);
  };

}
