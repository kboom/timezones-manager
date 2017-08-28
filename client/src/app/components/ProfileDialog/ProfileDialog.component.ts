import {Component, Inject} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MD_DIALOG_DATA, MdDialogRef, MdSnackBar} from "@angular/material";
import {SecurityService} from "../../+security/security.service";
import {omit} from "lodash-es";
import {validatorFor} from "../../validators/common.validators";
import {EMAIL_REGEX, USERNAME_REGEX, PASSWORD_REGEX} from "../../validators/validation.rules";
import {fieldsAreEqual} from "../../validators/composite.validators";
import {Entity} from "../../models/hateoas/Entity.model";
import {UserModel} from "../../models/User.model";

@Component({
    selector: 'profileDialog',
    template: `

        <div>

            <h2 md-dialog-title>Edit profile</h2>
            
            <md-dialog-content fxLayout='column'>

                <md-input-container>
                    <input mdInput disabled type="text" placeholder="Username" [value]="userEntity.entity.username">
                </md-input-container>

                <md-input-container>
                    <input mdInput disabled type="email" placeholder="E-mail" [value]="userEntity.entity.email">
                </md-input-container>

                <form [formGroup]="detailsChangeForm" fxLayout='column' (ngSubmit)="this.doRegister($event)">
                    
                    <md-input-container>
                        <input mdInput formControlName="oldPassword" type="password" placeholder="Old password">
                    </md-input-container>
                    <control-messages [control]="detailsChangeForm.controls.oldPassword"></control-messages>

                    <md-input-container>
                        <input mdInput formControlName="newPassword" type="password" placeholder="New password">
                    </md-input-container>
                    <control-messages [control]="detailsChangeForm.controls.newPassword"></control-messages>

                    <md-input-container>
                        <input mdInput formControlName="newPasswordRepeated" type="password"
                               placeholder="Repeat new password">
                    </md-input-container>
                    <control-messages [control]="detailsChangeForm.controls.newPasswordRepeated"></control-messages>


                </form>

            </md-dialog-content>

            <md-dialog-actions>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" class="mat-primary" md-button [disabled]="!detailsChangeForm.valid">Update
                </button>
            </md-dialog-actions>

        </div>


    `
})
// https://codecraft.tv/courses/angular/forms/model-driven-validation/
export class ProfileDialog {

    detailsChangeForm: FormGroup;

    constructor(@Inject(MD_DIALOG_DATA) public userEntity: Entity<UserModel>,
                private fb: FormBuilder,
                private dialogRef: MdDialogRef<ProfileDialog>,
                private authService: SecurityService,
                private snackBar: MdSnackBar) {
        this.detailsChangeForm = this.fb.group({
            oldPassword: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            newPassword: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            newPasswordRepeated: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
        }, {
            validator: fieldsAreEqual("newPassword", "newPasswordRepeated", "password.equality")
        });
    }

    doRegister(event): void {
        let formData = this.detailsChangeForm.value;
        // this.authService.updateAccount(omit(formData, ['passwordRepeated']))
        //     .subscribe((x) => {
        //         const snackBarRef = this.snackBar.open("Please check your mailbox", "Close", {
        //             duration: 5000,
        //         });
        //         snackBarRef.onAction().subscribe(() => {
        //             snackBarRef.dismiss()
        //         });
        //         this.dialogRef.close(x)
        //     })
    }

}
