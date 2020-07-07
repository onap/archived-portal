import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuditLogService {

  constructor(private api: HttpClient) { }

  storeAudit(affectedAppId:any,type:any,comment:any): Observable<any> {
    var url = environment.api.storeAuditLog+'?affectedAppId=' + affectedAppId;
        	if(type!=''){
        		url= url+'&type='+type;
        	}
        	if(comment!=''){
        		url= url+'&comment='+comment;
        	}
    return this.api.get(url);
  }
}
