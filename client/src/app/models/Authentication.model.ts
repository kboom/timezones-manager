import {UserModel} from "./User.model";

export class AuthenticationModel {
    token: String;
    user: UserModel;

    isAuthenticated() : Boolean {
        return !!this.token
    }

    static noAuthentication(): AuthenticationModel {
        return new AuthenticationModel()
    }

}