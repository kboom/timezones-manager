import {Component, OnDestroy, OnInit} from "@angular/core";
import {SecurityService} from "../../+security/security.service";
import {Subscription} from "rxjs/Subscription";
import {MdDialog} from "@angular/material";
import {SignInDialogComponent} from "../SignInDialog/SignInDialog.component";
import {SecurityContextHolder} from "../../+security/security.context";
import {Observable} from "rxjs/Observable";
import {RegistrationDialogComponent} from "../RegistrationDialog/RegistrationDialog.component";
import {Router} from "@angular/router";

@Component({
    selector: 'userMenu',
    template: `

        <div *ngIf="this.isLoggedIn$ | async; else signInBtn">
            <button md-button [mdMenuTriggerFor]="menu">Hello, {{ this.securityContext.getAuthentication().getUsername()
                }}!
            </button>
            <md-menu #menu="mdMenu">
                <button md-menu-item (click)="this.authService.signOut()">Sign out</button>
            </md-menu>
        </div>

        <ng-template #signInBtn>
            <button md-button (click)="this.openSignInDialog()">Sign in</button>
            <button md-button class="mat-primary" (click)="this.openRegistrationDialog()">Register</button>
        </ng-template>


    `
})
export class UserMenuComponent implements OnInit, OnDestroy {

    authenticationEvents$: Subscription;

    isLoggedIn$: Observable<Boolean>;

    constructor(private authService: SecurityService,
                private securityContext: SecurityContextHolder,
                private dialog: MdDialog,
                private router: Router) {
        this.isLoggedIn$ = this.securityContext.getAuthentication$()
            .map((authentication) => authentication.isAuthenticated())
    }

    handleAuthenticationEvent(event) {
        console.log("Authentication event");
    }

    openRegistrationDialog() {
        this.dialog.open(RegistrationDialogComponent).afterClosed()
            .subscribe(result => {

            });
    }

    openSignInDialog() {
        this.dialog.open(SignInDialogComponent).afterClosed()
            .subscribe(result => {
                this.router.navigate(['home']);
            });
    }

    public ngOnInit() {
        this.authService.getAuthenticationEvents$().subscribe(this.handleAuthenticationEvent)
    }

    public ngOnDestroy() {
        // this.authenticationEvents$.unsubscribe()
    }

}
