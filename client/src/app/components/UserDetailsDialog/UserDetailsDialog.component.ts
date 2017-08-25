import {Component, Inject} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {SecurityService} from "../../+security/security.service";
import {MD_DIALOG_DATA} from '@angular/material';
import {UserModel} from "../../models/User.model";
import { pick } from 'lodash-es';

@Component({
    selector: 'userDetailsDialog',
    template: `

        <form [formGroup]="userDetailsForm" (ngSubmit)="this.doLogin($event)">

            <h2 md-dialog-title>Update user</h2>
            
            <md-dialog-content fxLayout='column'>

                <md-input-container>
                    <input mdInput formControlName="username" type="text" placeholder="Username">
                </md-input-container>

                <md-input-container>
                    <input mdInput formControlName="password" type="password" placeholder="Password">
                </md-input-container>

                <md-input-container>
                    <input mdInput formControlName="email" type="email" placeholder="E-mail">
                </md-input-container>
                
                <md-checkbox formControlName="enabled">Enabled</md-checkbox>

            </md-dialog-content>

            <md-dialog-actions>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" md-button>Update</button>
            </md-dialog-actions>

        </form>

    `
})
export class UserDetailsDialogComponent {

    userDetailsForm: FormGroup;

    constructor(
        @Inject(MD_DIALOG_DATA) private initialValues: UserModel,
        private fb: FormBuilder,
        private dialogRef: MdDialogRef<UserDetailsDialogComponent>,
        private authService: SecurityService
    ) {
        this.userDetailsForm = this.fb.group({
            username: ["", Validators.required],
            password: ["", Validators.required],
            email: ["", Validators.required],
            enabled: ["", Validators.required],
        });

        this.userDetailsForm.setValue(pick(initialValues, ['username', 'password', 'email', 'enabled']));
    }

    doLogin(event): void {
        let formData = this.userDetailsForm.value;
        // this.authService.authenticate(formData)
        //     .subscribe((x) => this.dialogRef.close(x))
    }

}
