import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {Injectable} from "@angular/core";
import {UserModel} from "../models/User.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {EntityCollectionModel, EntityFactory} from "../models/hateoas/EntityCollection.model";
import {RoleModel} from "../models/Role.model";
import {Entity, ModelFactory} from "../models/hateoas/Entity.model";

const getAllUsersURL = "http://localhost:8080/api/users?projection=withDetails";

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
        return this.http.put(userEntity.links['self']['href'], userEntity.entity)
            .map((body: any) => this.userFactory.constructEntity(body))
    }

}
