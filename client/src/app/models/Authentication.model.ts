import {UserModel} from "./User.model";
import {TokenCodesModel} from "./Token.model";

export class AuthenticationModel {
    token: TokenCodesModel;
    user: UserModel;

    isAuthenticated() : Boolean {
        return !!this.token
    }

    static noAuthentication(): AuthenticationModel {
        return new AuthenticationModel()
    }

}