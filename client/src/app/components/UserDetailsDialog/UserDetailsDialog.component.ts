import {Component, Inject} from "@angular/core";
import {MD_DIALOG_DATA, MdDialogRef} from "@angular/material";
import {UserModel} from "../../models/User.model";
import {extend, includes, map, pick, transform} from "lodash-es";
import {UserRepository} from "../../repository/user.repository";
import {Entity} from "../../models/hateoas/Entity.model";
import {UserEntityManager} from "src/app/components/UserDetailsForm/UserDetailsForm.component";
import {Observable} from "rxjs/Observable";

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
export class UserDetailsDialogComponent implements UserEntityManager {

    constructor(@Inject(MD_DIALOG_DATA) public userEntity: Entity<UserModel>,
                private dialogRef: MdDialogRef<UserDetailsDialogComponent>,
                private userRepository: UserRepository) {

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
