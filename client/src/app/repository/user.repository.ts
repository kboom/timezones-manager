import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/observable/zip";
import "rxjs/add/operator/map";
import {Injectable} from "@angular/core";
import {UserModel} from "../models/User.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {EntityCollectionModel} from "../models/hateoas/EntityCollection.model";
import {RoleModel} from "../models/Role.model";
import {Entity} from "../models/hateoas/Entity.model";
import {extend, omit, transform} from "lodash-es";
import {UserFactory} from "../models/factory";

const getAllUsersURL = "http://localhost:8080/api/users?projection=withDetails";
const postUserURL = "http://localhost:8080/api/users";
const getUserByUsernameURL = "http://localhost:8080/api/users/search/findByUsername?projections=withDetails";

@Injectable()
export class UserRepository {

    constructor(private userFactory: UserFactory,
                private http: HttpClient) {

    }

    public getUserByUsername(username: string): Observable<EntityCollectionModel<UserModel>> {
        return this.http.get(getUserByUsernameURL, { params: new HttpParams().set("username", username) })
            .map((body: any) => this.userFactory.constructEntity(body))
            .catch(() => Observable.throw("Could not get user"));
    }

    public getAllUsers(): Observable<EntityCollectionModel<UserModel>> {
        return this.http.get(getAllUsersURL)
            .map((body: any) => new EntityCollectionModel('users', body, this.userFactory))
            .catch(() => Observable.throw("Could not get users"));
    }

    createUser(userEntity: Entity<UserModel>): Observable<any> {
        return this.http.post(postUserURL, transform(userEntity.entity, (result, value, key) => {
            if(key == 'authorities') {
                result[key] = value.map((val) => `/authorities/${RoleModel[val]}`)
            } else {
                result[key] = value;
            }
        }, {}));
    }

    public updateUser(userEntity: Entity<UserModel>): Observable<Entity<UserModel>> {
        return Observable.create((observer) => {
            const userUpdate$ = this.http.patch(userEntity.links['self']['href'], omit(userEntity.entity, ['authorities']), {
                headers: new HttpHeaders().set("Content-Type", "application/merge-patch+json")
            });

            const privilegeUpdate$ = this.http.put(userEntity.links['authorities']['href'],
                this.constructRoleURIsFor(userEntity), {
                    headers: new HttpHeaders().set("Content-Type", "text/uri-list")
                });

            Observable.zip(userUpdate$, privilegeUpdate$).subscribe(() => observer.next(),
                () => observer.error("Could not update user"));
        });
    }

    public deleteUser(userEntity: Entity<UserModel>) {
        return this.http.delete(userEntity.links['self']['href']);
    }

    private constructRoleURIsFor(userEntity: Entity<UserModel>) {
        const basePath = "http://localhost:8080/api/authorities/:roleName";
        return userEntity.entity.authorities.map((role) => basePath.replace(":roleName", RoleModel[role])).join("\n");
    }

}
