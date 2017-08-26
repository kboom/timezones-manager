import {Component, Inject, ViewChild} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MD_DIALOG_DATA, MdDialogRef} from "@angular/material";
import {UserModel} from "../../models/User.model";
import {extend, includes, map, pick, transform} from "lodash-es";
import {UserRepository} from "../../repository/user.repository";
import {Entity} from "../../models/hateoas/Entity.model";
import {RoleModelAware} from "../../models/Role.model";
import {WithEnumEx} from "../../utils/enum.utils";
import {ValidationService} from "../../services/validation.service";
import {UserEntityManager} from "src/app/components/UserDetailsForm/UserDetailsForm.component";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";

@Component({
    selector: 'userDetailsDialog',
    template: `

        <div>

            <h2 md-dialog-title>Update user</h2>

            <md-dialog-content>

                <userDetailsForm [userEntity]="userEntity" [entityManager]="this"></userDetailsForm>

            </md-dialog-content>

            <md-dialog-actions>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" md-button form="userDetailsForm">Update</button>
            </md-dialog-actions>

        </div>

    `
})
// [disabled]="!userDetailsForm.valid"
@RoleModelAware
@WithEnumEx
export class UserDetailsDialogComponent implements UserEntityManager {

    userDetailsForm: FormGroup;

    constructor(@Inject(MD_DIALOG_DATA) public userEntity: Entity<UserModel>,
                private fb: FormBuilder,
                private dialogRef: MdDialogRef<UserDetailsDialogComponent>,
                private userRepository: UserRepository) {
        this.userDetailsForm = this.fb.group({
            username: ["", Validators.required],
            password: ["", Validators.required, ValidationService.passwordValidator],
            email: ["", Validators.required, ValidationService.emailValidator],
            enabled: [false, Validators.required],
            roles: this.fb.array([
                [false],
                [false],
                [false]
            ])
        });
    }

    onSubmit(userEntity: Entity<UserModel>): Observable<any> {
        return Observable.create((observer) => {
            this.userRepository.updateUser(userEntity)
                .subscribe(() => {
                    observer.complete();
                    this.dialogRef.close();
                });
        });
    }

}
