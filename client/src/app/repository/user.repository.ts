import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/observable/zip";
import "rxjs/add/operator/map";
import {Injectable} from "@angular/core";
import {UserModel} from "../models/User.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {EntityCollectionModel, EntityFactory} from "../models/hateoas/EntityCollection.model";
import {RoleModel} from "../models/Role.model";
import {Entity, ModelFactory} from "../models/hateoas/Entity.model";
import {extend, omit} from 'lodash-es';
import {HttpHeaders} from "@angular/common/http";

const getAllUsersURL = "http://localhost:8080/api/users?projection=withDetails";
const getAllRolesURL = "http://localhost:8080/api/roles?projection=withDetails";

class UserFactory implements EntityFactory<UserModel>, ModelFactory<UserModel> {

    constructModel = (obj: any): UserModel => {
        return new UserModel(
            obj.username,
            obj.email,
            obj.enabled,
            (obj.authorities || []).map((authority) => RoleModel[authority.name])
        )
    };

    constructEntity = (obj: any): Entity<UserModel> => {
        return Entity.fromJSON(obj, this);
    };

}

@Injectable()
export class UserRepository {

    private userFactory = new UserFactory();

    constructor(private http: HttpClient) {

    }

    public getAllUsers(): Observable<EntityCollectionModel<UserModel>> {
        return this.http.get(getAllUsersURL)
            .map((body: any) => new EntityCollectionModel('users', body, this.userFactory))
            .catch(() => Observable.throw("Could not get users"));
    }

    public updateUser(userEntity: Entity<UserModel>): Observable<Entity<UserModel>> {
        return Observable.create((observer) => {
            const userUpdate$ = this.http.put(userEntity.links['self']['href'], omit(userEntity.entity, ['roles']));

            const privilegeUpdate$ = this.http.put(userEntity.links['authorities']['href'],
                this.constructRoleURIsFor(userEntity), {
                    headers: new HttpHeaders().set("Content-Type", "text/uri-list")
                });

            Observable.zip(userUpdate$, privilegeUpdate$).subscribe(() => observer.next(),
                () => observer.error("Could not update user"));
        });
    }

    private constructRoleURIsFor(userEntity: Entity<UserModel>) {
        const basePath = "http://localhost:8080/api/privileges/:roleName";
        return userEntity.entity.roles.map((role) => basePath.replace(":roleName", RoleModel[role])).join(",");
    }

}
