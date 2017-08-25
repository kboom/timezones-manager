import {BehaviorSubject} from "rxjs/BehaviorSubject";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {Injectable} from "@angular/core";
import {UserModel} from "../models/User.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {EntityCollectionModel, EntityFactory} from "../models/EntityCollection.model";
import {RoleModel} from "../models/Role.model";

const getAllUsersURL = "http://localhost:8080/api/users?projection=withDetails";

class UserFactory implements EntityFactory<UserModel> {

    construct(obj: any): UserModel {
        return new UserModel(
            obj.username,
            obj.email,
            obj.enabled,
            obj.authorities.map((authority) => RoleModel[authority.name])
        )
    }

}

@Injectable()
export class UserRepository {

    private userFactory = new UserFactory();

    constructor(private http: HttpClient) {

    }

    public getAllUsers(): Observable<EntityCollectionModel<UserModel>> {
        return this.http.get(getAllUsersURL)
            .map((response: any) => new EntityCollectionModel('users', response, this.userFactory))
            .catch(() => Observable.throw("Could not get users"));
    }

}
