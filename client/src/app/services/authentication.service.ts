import {Injectable} from '@angular/core';
import {Http} from "@angular/http";
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/defer';
import {AuthenticationModel} from "../models/Authentication.model";

@Injectable()
export class AuthenticationService {

    authentication: AuthenticationModel = AuthenticationModel.noAuthentication();

    constructor(private http: Http) {

    }

    isAuthenticated(): Observable<Boolean> {
        return Observable.defer(() => Observable.of(this.authentication.isAuthenticated()))
    }

}
