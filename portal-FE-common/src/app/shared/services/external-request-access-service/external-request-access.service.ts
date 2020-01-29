import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExternalRequestAccessService {



  constructor(private api: HttpClient) { }

  getExternalRequestAccessServiceInfo(): Observable<any> {
    return this.api.get(environment.api.externalRequestAccessSystem);
  }

}
