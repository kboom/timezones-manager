import {Component, OnDestroy, OnInit} from "@angular/core";
import {Title} from "./title";
import {XLargeDirective} from "./x-large";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {SecurityService} from "../../+security/security.service";
import {Observable} from "rxjs/Observable";
import {SignInDialogComponent} from "../../components/SignInDialog/SignInDialog.component";
import {MdDialog} from "@angular/material";

enum ConfirmationStatus {
    PENDING,
    SUCCESSFUL,
    FAILED
}


@Component({
    selector: 'registrationPage',
    template: `        

        <div [ngSwitch]="confirmationStatus">
            <div *ngSwitchCase="ConfirmationStatus.PENDING"
                 fxLayout='column'
                 fxLayoutAlign="center center">
                <md-spinner></md-spinner>
                <h3>Registration request is being verified.</h3>
            </div>
            <md-card *ngSwitchCase="ConfirmationStatus.SUCCESSFUL">
                <md-card-header>
                    <md-card-title>Success</md-card-title>
                    <md-card-subtitle>Your account has been successfully confirmed!</md-card-subtitle>
                </md-card-header>
                <md-card-actions>
                    <button class="mat-primary" (click)="openSignInDialog()" md-button>Sign in</button>
                </md-card-actions>
            </md-card>
            <md-card *ngSwitchCase="ConfirmationStatus.FAILED" fxLayout='column'>
                <h3>Your account could not be verified!</h3>
                <div fxLayout='row' fxLayoutAlign="center center" fxLayoutGap="20px">
                    <div>Try again later</div>
                    <div>or</div>
                    <button md-button>Send a new validation link</button>
                </div>
            </md-card>
        </div>

    `
})
export class RegistrationComponent implements OnInit, OnDestroy {

    ConfirmationStatus = ConfirmationStatus;

    confirmationStatus: ConfirmationStatus = ConfirmationStatus.PENDING;

    confirmation$$: Subscription;
    queryParams$$: Subscription;

    constructor(private route: ActivatedRoute,
                private securityService: SecurityService,
                private dialog: MdDialog) {

    }

    public ngOnInit() {
        this.queryParams$$ = this.route.queryParams
            .subscribe(params => {
                const confirmationCode = params['confirmation'];
                this.confirmation$$ = this.securityService.confirmAccount(confirmationCode)
                    .subscribe(this.handleConfirmationSuccess, this.handleConfirmationFailure)
            });
    }

    openSignInDialog = () => {
        this.dialog.open(SignInDialogComponent).afterClosed()
            .subscribe(result => {
                console.log(`Dialog result: ${result}`);
            });
    };

    handleConfirmationSuccess = () => {
        this.confirmationStatus = ConfirmationStatus.SUCCESSFUL;
    };

    handleConfirmationFailure = () => {
        this.confirmationStatus = ConfirmationStatus.FAILED;
    };

    ngOnDestroy() {
        this.queryParams$$.unsubscribe();
        if (this.confirmation$$) {
            this.confirmation$$.unsubscribe()
        }
    }

}
