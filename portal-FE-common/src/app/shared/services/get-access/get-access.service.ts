import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GetAccessService {

  constructor(private http: HttpClient) { }
  apiUrl = environment.api;

  getListOfApp() {
    return this.http.get(this.apiUrl.listOfApp);
  }
}
