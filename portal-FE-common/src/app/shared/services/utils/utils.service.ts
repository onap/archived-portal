import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {

  constructor() { }

  isValidJSON(json) {
    try {
        var checkJSON = JSON.parse(JSON.stringify(json));
        if (checkJSON && typeof checkJSON === 'object' && checkJSON !== null) {
            // this.$log.debug('UtilsService::isValidJSON: JSON is valid!');
            return true;
        }
    } catch (err) {
        // this.$log.debug('UtilsService::isValidJSON: json passed is not valid: ' + JSON.stringify(err));
    }
    return false;
}
}
