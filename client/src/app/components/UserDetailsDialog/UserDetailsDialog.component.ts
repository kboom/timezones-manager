import {Component, Inject} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {MD_DIALOG_DATA} from '@angular/material';
import {UserModel} from "../../models/User.model";
import {pick, map, extend, includes, transform} from 'lodash-es';
import {UserRepository} from "../../repository/user.repository";
import {Entity} from "../../models/hateoas/Entity.model";
import {RoleModel, RoleModelAware} from "../../models/Role.model";
import {EnumEx, WithEnumEx} from "../../utils/enum.utils";
import {ValidationService} from "../../services/validation.service";

@Component({
    selector: 'userDetailsDialog',
    template: `

        <form [formGroup]="userDetailsForm" (ngSubmit)="this.doUpdate($event)">

            <h2 md-dialog-title>Update user</h2>

            <md-dialog-content fxLayout='column'>

                <md-input-container>
                    <input mdInput formControlName="username" type="text" placeholder="Username">
                </md-input-container>
                <control-messages [control]="userDetailsForm.controls.username"></control-messages>

                <md-input-container>
                    <input mdInput formControlName="password" type="password" placeholder="Password">
                </md-input-container>
                <control-messages [control]="userDetailsForm.controls.password"></control-messages>

                <md-input-container>
                    <input mdInput formControlName="email" type="email" placeholder="E-mail">
                </md-input-container>
                <control-messages [control]="userDetailsForm.controls.email"></control-messages>

                <md-checkbox formControlName="enabled">Enabled</md-checkbox>

                <div style="height: 35px;"></div>

                <div formArrayName="roles">
                    <md-checkbox [formControlName]="RoleModel[roleName]"
                                 *ngFor="let roleName of EnumEx.getNames(RoleModel)">
                        {{ roleName }}
                    </md-checkbox>
                </div>

                <div style="height: 35px;"></div>

            </md-dialog-content>

            <md-dialog-actions>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" md-button [disabled]="!userDetailsForm.valid">Update</button>
            </md-dialog-actions>

            <pre>{{ userDetailsForm.value | json }}</pre>

        </form>

    `
})
@RoleModelAware
@WithEnumEx
export class UserDetailsDialogComponent {

    userDetailsForm: FormGroup;

    constructor(@Inject(MD_DIALOG_DATA) private userEntity: Entity<UserModel>,
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

        this.userDetailsForm.setValue(
            extend({}, pick(userEntity.entity, ['username', 'password', 'email', 'enabled']), {
                roles: EnumEx.getValues(RoleModel).map((role) => {
                    return includes(userEntity.entity.roles, role);
                })
            })
        );
    }

    doUpdate(event): void {
        const formData = this.userDetailsForm.value;
        this.userRepository.updateUser(this.userEntity.withUpdatedEntity(
            transform(formData, (result, value, key) => {
                if(key == 'roles') {
                    result[key] = EnumEx.getValues(RoleModel).filter((role) => value[role])
                } else {
                    result[key] = value;
                }
            }, {})
        )).subscribe(() => this.dialogRef.close())
    }

}
