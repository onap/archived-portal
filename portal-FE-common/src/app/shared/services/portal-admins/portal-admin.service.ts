import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PortalAdminsService {
  apiUrl = environment.api;
  constructor(private http: HttpClient) { }

  getPortalAdmins() {
    return this.http.get(this.apiUrl.portalAdmins);
  }

  addPortalAdmin(selectedUser: string) {
    return this.http.post(this.apiUrl.portalAdmin, selectedUser);
  }

  removePortalAdmin(userId: number, orUserId: string) {
    let userInfo = userId + "-" + orUserId;
    return this.http.delete(this.apiUrl.portalAdmin + '/' + userInfo);
  }

}
