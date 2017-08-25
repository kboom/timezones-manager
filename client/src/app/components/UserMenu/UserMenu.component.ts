import {Component, OnDestroy, OnInit} from "@angular/core";
import {SecurityService} from "../../+security/security.service";
import {Subscription} from "rxjs/Subscription";
import {MdDialog} from "@angular/material";
import {SignInDialogComponent} from "../SignInDialog/SignInDialog.component";
import {SecurityContextHolder} from "../../+security/security.context";
import {Observable} from "rxjs/Observable";

@Component({
    selector: 'userMenu',
    template: `

        <div *ngIf="this.isLoggedIn$ | async; else signInBtn">
            <button md-button [mdMenuTriggerFor]="menu">Hello, {{ this.securityContext.getAuthentication().getUsername() }}!
            </button>
            <md-menu #menu="mdMenu">
                <button md-menu-item>Item 1</button>
                <button md-menu-item>Item 2</button>
            </md-menu>
        </div>

        <ng-template #signInBtn>
            <button md-button (click)="this.openSignInDialog()">Sign in</button>
        </ng-template>


    `
})
export class UserMenuComponent implements OnInit, OnDestroy {

    authenticationEvents$: Subscription;

    isLoggedIn$: Observable<Boolean>;

    constructor(private authService: SecurityService,
                private securityContext: SecurityContextHolder,
                private dialog: MdDialog) {
        this.isLoggedIn$ = this.securityContext.getAuthentication$()
            .map((authentication) => authentication.isAuthenticated())
    }

    handleAuthenticationEvent(event) {
        console.log("Authentication event");
    }

    openSignInDialog() {
        const dialog = this.dialog.open(SignInDialogComponent);
        dialog.afterClosed().subscribe(result => {
            console.log(`Dialog result: ${result}`);
        });
    }

    public ngOnInit() {
        this.authService.getAuthenticationEvents$().subscribe(this.handleAuthenticationEvent)
    }

    public ngOnDestroy() {
        this.authenticationEvents$.unsubscribe()
    }

}
