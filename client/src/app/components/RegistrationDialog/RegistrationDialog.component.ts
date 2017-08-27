import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {SecurityService} from "../../+security/security.service";
import {omit} from "lodash-es";

@Component({
    selector: 'signInDialog',
    template: `

        <form [formGroup]="registrationForm" (ngSubmit)="this.doRegister($event)">

            <h2 md-dialog-title>Please sign in</h2>

            <md-dialog-content fxLayout='column'>

                <md-input-container>
                    <input mdInput formControlName="username" type="text" placeholder="Username">
                </md-input-container>

                <md-input-container>
                    <input mdInput formControlName="email" type="email" placeholder="E-mail">
                </md-input-container>

                <md-input-container>
                    <input mdInput formControlName="password" type="password" placeholder="Password">
                </md-input-container>

                <md-input-container>
                    <input mdInput formControlName="passwordRepeated" type="password" placeholder="Repeat password">
                </md-input-container>

            </md-dialog-content>

            <md-dialog-actions>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" class="mat-primary" md-button>Create account</button>
            </md-dialog-actions>

        </form>

    `
})
// https://codecraft.tv/courses/angular/forms/model-driven-validation/
export class RegistrationDialogComponent {

    registrationForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private dialogRef: MdDialogRef<RegistrationDialogComponent>,
        private authService: SecurityService
    ) {
        this.registrationForm = this.fb.group({
            username: ["", Validators.required],
            email: ["", Validators.required],
            password: ["", Validators.required],
            passwordRepeated: ["", Validators.required],
        });
    }

    doRegister(event): void {
        let formData = this.registrationForm.value;
        this.authService.registerAccount(omit(formData, ['passwordRepeated']))
            .subscribe((x) => this.dialogRef.close(x))
    }

}
