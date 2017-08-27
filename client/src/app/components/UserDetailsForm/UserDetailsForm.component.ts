import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserModel} from "../../models/User.model";
import {extend, includes, map, pick, transform} from "lodash-es";
import {Entity} from "../../models/hateoas/Entity.model";
import {RoleModel, RoleModelAware} from "../../models/Role.model";
import {EnumEx, WithEnumEx} from "../../utils/enum.utils";
import {Observable} from "rxjs/Observable";
import {EMAIL_REGEX, PASSWORD_REGEX, USERNAME_REGEX} from "../../validators/validation.rules";
import {atLeastOneTrue, validatorFor} from "../../validators/common.validators";

export interface UserEntityManager {

    onSubmit(userEntity: Entity<UserModel>): Observable<any>

}

@Component({
    selector: 'userDetailsForm',
    template: `

        <form id="userDetailsForm" [formGroup]="userDetailsForm" (ngSubmit)="this.onSubmit($event)" fxLayout='column'>

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

            <md-slide-toggle formControlName="enabled">Enabled</md-slide-toggle>

            <div style="height: 35px;"></div>

            <div formArrayName="authorities" fxLayout='row wrap' fxLayoutAlign='space-between center'
                 fxLayoutGap="20px" style="padding-bottom: 15px">
                <md-checkbox [formControlName]="RoleModel[roleName]"
                             *ngFor="let roleName of EnumEx.getNames(RoleModel)">
                    {{ roleName }}
                </md-checkbox>
            </div>
            <control-messages [control]="userDetailsForm.controls.authorities"></control-messages>
            
        </form>

    `
})
@RoleModelAware
@WithEnumEx
export class UserDetailsFormComponent implements OnInit {

    public userDetailsForm: FormGroup;

    @Input()
    private userEntity: Entity<UserModel>;

    @Input()
    private entityManager: UserEntityManager;

    constructor(private fb: FormBuilder) {
        this.userDetailsForm = this.fb.group({
            username: ["", Validators.required, validatorFor(USERNAME_REGEX)],
            password: ["", Validators.required, validatorFor(PASSWORD_REGEX)],
            email: ["", Validators.required, validatorFor(EMAIL_REGEX)],
            enabled: [false, Validators.required],
            authorities: this.fb.array([
                [false],
                [false],
                [false]
            ], atLeastOneTrue('role:atLeastOne'))
        });
    }

    ngOnInit(): void {
        this.setValues(this.userEntity);
    }

    setValues = (userEntity: Entity<UserModel>) => {
        this.userDetailsForm.setValue(
            extend({}, pick(userEntity.entity, ['username', 'password', 'email', 'enabled']), {
                authorities: EnumEx.getValues(RoleModel).map((role) => {
                    return includes(userEntity.entity.authorities, role);
                })
            })
        );
    };

    onSubmit(event): void {
        const formData = this.userDetailsForm.value;
        this.entityManager.onSubmit(this.userEntity.withUpdatedEntity(transform(formData, (result, value, key) => {
            if (key == 'authorities') {
                result[key] = EnumEx.getValues(RoleModel).filter((role) => value[role])
            } else {
                result[key] = value;
            }
        }, {}))).subscribe()
    }

}
