/**
 * Angular 2 decorators and services
 */
import {
    Component,
    OnInit,
    ViewEncapsulation
} from '@angular/core';
import {AppState} from './app.service';

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
                   routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                    Users
                </a>
                <span class="fill-remaining-space"></span>
                <button md-button [mdMenuTriggerFor]="menu">Menu</button>
            </md-toolbar>
            <md-menu #menu="mdMenu">
                <button md-menu-item>Item 1</button>
                <button md-menu-item>Item 2</button>
            </md-menu>
        </nav>

        <md-menu #menu="mdMenu">
            <button md-menu-item>
                <md-icon> dialpad</md-icon>
                <span> Redial </span>
            </button>
            <button md-menu-item disabled>
                <md-icon> voicemail</md-icon>
                <span> Check voicemail </span>
            </button>
            <button md-menu-item>
                <md-icon> notifications_off</md-icon>
                <span> Disable alerts </span>
            </button>
        </md-menu>

        <main>
            <router-outlet></router-outlet>
        </main>

    `
})
export class AppComponent implements OnInit {
    public angularclassLogo = 'assets/img/angularclass-avatar.png';
    public name = 'Angular 2 Webpack Starter';
    public url = 'https://twitter.com/AngularClass';

    constructor(public appState: AppState) {
    }

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
