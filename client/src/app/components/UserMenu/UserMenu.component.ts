import {Component, OnInit} from "@angular/core";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
    selector: 'userMenu',
    template: `

        <div *ngIf="this.authService.isAuthenticated() | async">
            <button md-button [mdMenuTriggerFor]="menu">Menu</button>
            <md-menu #menu="mdMenu">
                <button md-menu-item>Item 1</button>
                <button md-menu-item>Item 2</button>
            </md-menu>
        </div>
        
    `,
})
export class UserMenuComponent implements OnInit {

    constructor(private authService: AuthenticationService) {}

    public ngOnInit() {
        console.log('hello `Detail` component');
    }

}
