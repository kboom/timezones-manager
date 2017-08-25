/**
 * Angular 2 decorators and services
 */
import {Component, OnInit, ViewEncapsulation} from "@angular/core";
import {AppState} from "./app.service";
import {SecurityService} from "./+security/security.service";
import {RoleModelAware} from "./models/Role.model";
import {SecurityContextHolder} from "./+security/security.context";

/**
 * App Component
 * Top Level Component
 */
@Component({
    selector: 'app',
    encapsulation: ViewEncapsulation.None,
    styleUrls: [
        './app.component.css'
    ],
    template: `

        <nav>
            <md-toolbar>
                <a [routerLink]=" ['./home'] "
                   routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    Home
                </a>
                <a [routerLink]=" ['./timezones'] "
                   routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    Timezones
                </a>
                <a [routerLink]=" ['./users'] "
                   *ngIf="this.securityContext.getAuthentication().hasAnyRole(RoleModel.ROLE_ADMIN, RoleModel.ROLE_MANAGER)"
                   routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    Users
                </a>
                <span class="fill-remaining-space"></span>
                <userMenu>

                </userMenu>
            </md-toolbar>
        </nav>

        <main>
            <router-outlet></router-outlet>
        </main>

    `
})
@RoleModelAware
export class AppComponent implements OnInit {
    public angularclassLogo = 'assets/img/angularclass-avatar.png';
    public name = 'Angular 2 Webpack Starter';
    public url = 'https://twitter.com/AngularClass';

    constructor(
        private appState: AppState,
        private readonly securityService: SecurityService,
        private readonly securityContext: SecurityContextHolder
    ) {}

    public ngOnInit() {
        console.log('Initial App State', this.appState.state);
    }

}

/**
 * Please review the https://github.com/AngularClass/angular2-examples/ repo for
 * more angular app examples that you may copy/paste
 * (The examples may not be updated as quickly. Please open an issue on github for us to update it)
 * For help or questions please contact us at @AngularClass on twitter
 * or our chat on Slack at https://AngularClass.com/slack-join
 */
