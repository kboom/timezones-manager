import {Component, OnDestroy, OnInit} from "@angular/core";
import {AuthenticationService} from "../../services/authentication.service";
import {Subscription} from "rxjs/Subscription";

@Component({
    selector: 'userMenu',
    template: `

        <div *ngIf="this.authService.isAuthenticated() | async; else signInBtn">
            <button md-button [mdMenuTriggerFor]="menu">Menu</button>
            <md-menu #menu="mdMenu">
                <button md-menu-item>Item 1</button>
                <button md-menu-item>Item 2</button>
            </md-menu>
        </div>

        <ng-template #signInBtn>
            <button md-button>Sign in</button>
        </ng-template>
        
        
    `,
})
export class UserMenuComponent implements OnInit, OnDestroy {

    authenticationEvents$: Subscription;

    constructor(private authService: AuthenticationService) {}

    handleAuthenticationEvent(event) {
        console.log("Authentication event");
    }

    public ngOnInit() {
        this.authService.getAuthenticationEvents$().subscribe(this.handleAuthenticationEvent)
    }

    public ngOnDestroy() {
        this.authenticationEvents$.unsubscribe()
    }

}
