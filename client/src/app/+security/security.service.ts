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
import {SecurityContextHolder} from "./security.context";

enum AuthenticationEvent {
    SIGN_IN_FAILED
}

@Injectable()
export class SecurityService {

    private authenticationEventsEmitter: EventEmitter<AuthenticationEvent> = new EventEmitter();

    constructor(private securityContextHolder: SecurityContextHolder,
                private http: HttpClient,
                private responseMapper: ResponseMappingService) {

    }

    public getAuthenticationEvents$() {
        return this.authenticationEventsEmitter.asObservable();
    }

    public registerAccount({username, password, email}): Observable<any> {
        return this.http.post("http://localhost:8080/api/registration", JSON.stringify({username, password, email}))
            .catch((error: any) => Observable.throw(error));
    }

    public confirmAccount(confirmationCode): Observable<any> {
        return this.http.post("http://localhost:8080/api/registration/confirmation", JSON.stringify({ code: confirmationCode }))
            .catch((error: any) => Observable.throw(error));
    }

    public authenticate({username, password}): Observable<any> {
        const authentication$ = this.doAuthenticate(username, password);
        authentication$.subscribe((authentication) => {
            console.log(`Authenticated user ${authentication}`);
            this.securityContextHolder.setAuthentication(authentication);
        });
        return authentication$;
    }

    public signOut() {
        this.securityContextHolder.clearAuthentication();
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
        const securityContext = this.injector.get(SecurityContextHolder).getAuthentication();
        if (securityContext.isAuthenticated()) {
            return next.handle(req.clone({
                headers: req.headers.set('Authorization', securityContext.tokenCodes.accessToken)
            }));
        } else {
            return next.handle(req);
        }
    }

}
