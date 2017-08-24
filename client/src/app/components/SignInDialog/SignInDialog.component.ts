import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'signInDialog',
    template: `

        <form [formGroup]="loginForm" (ngSubmit)="this.doLogin($event)">

            <h2 md-dialog-title>Please sign in</h2>

            <md-dialog-content>

                <md-input-container>
                    <input mdInput formControlName="username" type="text" placeholder="Username">
                </md-input-container>

                <md-input-container>
                    <input mdInput formControlName="password" type="password" placeholder="Password">
                </md-input-container>

            </md-dialog-content>

            <md-dialog-actions>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" md-button>Sign in</button>
            </md-dialog-actions>

        </form>

    `
})
export class SignInDialogComponent {

    loginForm: FormGroup;

    constructor(private fb: FormBuilder) {
        this.loginForm = this.fb.group({
            username: ["", Validators.required],
            password: ["", Validators.required]
        });
    }

    doLogin(event): void {
        let formData = this.loginForm.value;
        alert(JSON.stringify(formData));
    }

}
