import {EventEmitter, Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/defer";
import {AuthenticationModel} from "../models/Authentication.model";
import {ResponseMappingService} from "./responseMapping.service";
import {TokenCodesModel} from "../models/Token.model";

enum AuthenticationEvent {
    SIGN_IN_FAILED
}

@Injectable()
export class AuthenticationService {

    private authenticationEventsEmitter: EventEmitter<AuthenticationEvent> = new EventEmitter();

    authentication: AuthenticationModel = AuthenticationModel.noAuthentication();

    constructor(private http: Http,
                private responseMapper: ResponseMappingService) {

    }

    getAuthenticationEvents$() {
        return this.authenticationEventsEmitter.asObservable();
    }

    authenticate({username, password}) {
        const tokenCodes$ = this.http.post("http://localhost:8080/api/auth", {})
            .map(this.responseMapper.mapIntoTokenCodes)
            .catch(() => this.authenticationEventsEmitter.emit(AuthenticationEvent.SIGN_IN_FAILED));

        tokenCodes$.subscribe((tokenCodes) => {
           console.log('abc')
        });

        return Observable.defer(() => tokenCodes$)
    }

    isAuthenticated(): Observable<Boolean> {
        return Observable.defer(() => Observable.of(this.authentication.isAuthenticated()))
    }

}
