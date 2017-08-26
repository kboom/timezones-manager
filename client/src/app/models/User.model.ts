import {RoleModel} from "./Role.model";
export class UserModel implements ModelEntity {

    constructor(
        public username: string,
        public email: string,
        public enabled: boolean = false,
        public authorities: RoleModel[] = [],
        public password: string = null,
    ) {}

    static emptyUser(): UserModel {
        return new UserModel(null, null);
    }

}