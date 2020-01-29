import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserbarService {
  refreshCount: number;
  maxCount: number;
  apiUrl = environment.api;
  constructor(public http: HttpClient) {
    this.refreshCount = 0;
    this.maxCount = 0;
  }

  getRefreshCount() {
    return this.refreshCount;
  }
  setRefreshCount(count) {
    this.refreshCount = count;
  }
  setMaxRefreshCount(count) {
    this.maxCount = count;
  }
  decrementRefreshCount() {
    this.refreshCount = this.refreshCount - 1;
  }

  getOnlineUserUpdateRate(){
    return this.http.get(this.apiUrl.onlineUserUpdateRate);
  }
}
