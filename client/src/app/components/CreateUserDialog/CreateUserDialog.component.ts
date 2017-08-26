import {Component, Inject} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {MD_DIALOG_DATA, MdDialogRef} from "@angular/material";
import {UserModel} from "../../models/User.model";
import {extend, includes, map, pick, transform} from "lodash-es";
import {UserRepository} from "../../repository/user.repository";
import {Entity} from "../../models/hateoas/Entity.model";
import {UserEntityManager} from "src/app/components/UserDetailsForm/UserDetailsForm.component";
import {Observable} from "rxjs/Observable";
import {UserFactory} from "../../models/factory/index";

@Component({
    selector: 'userDetailsDialog',
    template: `

        <div>

            <h2 md-dialog-title>Create user</h2>

            <md-dialog-content>

                <userDetailsForm [userEntity]="userEntity" [entityManager]="this"></userDetailsForm>

            </md-dialog-content>

            <md-dialog-actions>
                <button md-button md-dialog-close>Cancel</button>
                <button type="submit" md-button form="userDetailsForm">Create</button>
            </md-dialog-actions>

        </div>

    `
})
// [disabled]="!userDetailsForm.valid"
export class CreateUserDialogComponent implements UserEntityManager {

    userEntity: Entity<UserModel>;

    constructor(private dialogRef: MdDialogRef<CreateUserDialogComponent>,
                private userRepository: UserRepository,
    private userFactory: UserFactory) {
        this.userEntity = userFactory.constructNewEntity()
    }

    onSubmit(userEntity: Entity<UserModel>): Observable<any> {
        return Observable.create((observer) => {
            this.userRepository.createUser(userEntity)
                .subscribe(() => {
                    observer.complete();
                    this.dialogRef.close();
                });
        });
    }

}
