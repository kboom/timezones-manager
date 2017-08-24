import {EventEmitter, Injectable, Injector} from "@angular/core";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/defer";
import {AuthenticationModel} from "../models/Authentication.model";
import {ResponseMappingService} from "./responseMapping.service";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import "rxjs/add/observable/throw";
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";

enum AuthenticationEvent {
    SIGN_IN_FAILED
}

@Injectable()
export class SecurityService {

    private authenticationEventsEmitter: EventEmitter<AuthenticationEvent> = new EventEmitter();

    authentication: AuthenticationModel = AuthenticationModel.noAuthentication();

    constructor(private http: HttpClient,
                private responseMapper: ResponseMappingService) {

    }

    getAuthenticationEvents$() {
        return this.authenticationEventsEmitter.asObservable();
    }

    authenticate({username, password}): Observable<any> {
        // this.authenticationEventsEmitter.emit(AuthenticationEvent.SIGN_IN_FAILED)
        return this.http.post("http://localhost:8080/api/auth", JSON.stringify({ username, password }))
            .map((response: any) => this.responseMapper.mapIntoTokenCodes(response))
            .catch((error: any) => Observable.throw(error));

        // tokenCodes$.subscribe((tokenCodes) => {
        //    console.log('abc')
        // });
        //
        // return Observable.defer(() => tokenCodes$)
    }

    isAuthenticated(): Observable<Boolean> {
        return Observable.defer(() => Observable.of(this.authentication.isAuthenticated()))
    }

    // delete this
    isLoggedIn() {
        return this.authentication.isAuthenticated();
    }

}


@Injectable()
export class TokenAddingInterceptor implements HttpInterceptor {

    constructor(private injector: Injector) {

    }

    intercept (req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const securityService = this.injector.get(SecurityService);
        if(securityService.isLoggedIn()) {
            return next.handle(req.clone({
                headers: req.headers.set('Authorization', securityService.authentication.token.accessToken)
            }));
        } else {
            return next.handle(req);
        }
    }
}
