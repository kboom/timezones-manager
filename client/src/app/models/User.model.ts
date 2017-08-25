import {RoleModel} from "./Role.model";
export class UserModel {

    constructor(
        public username: string,
        public email: string,
        public enabled: boolean,
        public roles: RoleModel[] = [],
        public password: string = null,
    ) {}

}