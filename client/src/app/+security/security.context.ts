import {Injectable} from "@angular/core";
import {AuthenticationModel} from "../models/Authentication.model";
import {Subject} from "rxjs/Subject";
import "rxjs/add/observable/interval";
import {Observable} from "rxjs/Observable";

@Injectable()
export class SecurityContextHolder {

    private authentication: AuthenticationModel = AuthenticationModel.noAuthentication();

    private readonly authenticationSubject = new Subject();

    public getAuthentication$(): Observable<AuthenticationModel> {
        return this.authenticationSubject.asObservable();
    }

    public getAuthentication() {
        return this.authentication;
    }

    public setAuthentication(authentication: AuthenticationModel) {
        this.authentication = authentication;
        this.authenticationSubject.next(authentication);
    }

}