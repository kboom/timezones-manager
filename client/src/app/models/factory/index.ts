import {EntityFactory} from "../hateoas/EntityCollection.model";
import {UserModel} from "../User.model";
import {Entity, ModelFactory} from "../hateoas/Entity.model";
import {Injectable} from "@angular/core";
import {RoleModel} from "../Role.model";

@Injectable()
export class UserFactory implements EntityFactory<UserModel>, ModelFactory<UserModel> {

    constructNewModel(): UserModel {
        return UserModel.emptyUser();
    }

    constructNewEntity(): Entity<UserModel> {
        return Entity.empty(this.constructNewModel())
    }

    constructModel = (obj: any): UserModel => {
        return new UserModel(
            obj.username,
            obj.email,
            obj.enabled,
            (obj.authorities || []).map((authority) => RoleModel[authority.name])
        )
    };

    constructEntity = (obj: any | null): Entity<UserModel> => {
        return Entity.fromJSON(obj, this);
    };

}