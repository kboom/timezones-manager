import {Injectable} from '@angular/core';
import {Http} from "@angular/http";
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/defer';

@Injectable()
export class AuthenticationService {

    token: string;

    constructor(private http: Http) {

    }

    isAuthenticated(): Observable<Boolean> {
        return Observable.defer(() => Observable.of(!!this.token))
    }

}
