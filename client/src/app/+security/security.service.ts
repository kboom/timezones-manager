import {EventEmitter, Injectable, Injector} from "@angular/core";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/defer";
import {AuthenticationModel} from "../models/Authentication.model";
import {ResponseMappingService} from "./responseMapping.service";
import "rxjs/add/operator/map";
import "rxjs/add/operator/mergeMap";
import "rxjs/add/operator/catch";
import "rxjs/add/observable/throw";
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {UserModel} from "src/app/models/User.model";

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

    getAuthenticatedUser(): Observable<UserModel> {
        return null;
    }

    authenticate({username, password}): Observable<any> {
        const authentication$ = this.doAuthenticate(username, password)
        authentication$.subscribe((authentication) => {
            console.log(`Authenticated user ${authentication}`)
            this.authentication = authentication;
        });
        return authentication$;
    }

    isAuthenticated(): Observable<Boolean> {
        return Observable.defer(() => Observable.of(this.authentication.isAuthenticated()))
    }

    // delete this
    isLoggedIn() {
        return this.authentication.isAuthenticated();
    }

    private doAuthenticate(username, password): Observable<any> {
        return this.http.post("http://localhost:8080/api/auth", JSON.stringify({username, password}))
            .map((response: any) => this.responseMapper.mapIntoTokenCodes(response))
            .map(AuthenticationModel.authenticated)
            .catch((error: any) => Observable.throw(error));
    }

}


@Injectable()
export class TokenAddingInterceptor implements HttpInterceptor {

    constructor(private injector: Injector) {

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const securityService = this.injector.get(SecurityService);
        if (securityService.isLoggedIn()) {
            return next.handle(req.clone({
                headers: req.headers.set('Authorization', securityService.authentication.tokenCodes.accessToken)
            }));
        } else {
            return next.handle(req);
        }
    }
}
