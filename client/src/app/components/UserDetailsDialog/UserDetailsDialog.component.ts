import {Component, Inject} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {SecurityService} from "../../+security/security.service";
import {MD_DIALOG_DATA} from '@angular/material';
import {UserModel} from "../../models/User.model";
import { pick } from 'lodash-es';
import {UserRepository} from "../../repository/user.repository";
import {Entity} from "../../models/hateoas/Entity.model";

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
        @Inject(MD_DIALOG_DATA) private userEntity: Entity<UserModel>,
        private fb: FormBuilder,
        private dialogRef: MdDialogRef<UserDetailsDialogComponent>,
        private userRepository: UserRepository
    ) {
        this.userDetailsForm = this.fb.group({
            username: ["", Validators.required],
            password: ["", Validators.required],
            email: ["", Validators.required],
            enabled: ["", Validators.required],
        });

        this.userDetailsForm.setValue(pick(userEntity.entity, ['username', 'password', 'email', 'enabled']));
    }

    doLogin(event): void {
        const formData = this.userDetailsForm.value;
        this.userRepository.updateUser(this.userEntity.withUpdatedEntity(formData))
            .subscribe((x) => this.dialogRef.close(x))
    }

}
