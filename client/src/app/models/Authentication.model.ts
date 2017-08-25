import {UserModel} from "./User.model";
import {TokenCodesModel} from "./Token.model";

export class AuthenticationModel {

    constructor(readonly tokenCodes: TokenCodesModel) {
        this.tokenCodes = tokenCodes;
    }

    isAuthenticated() : Boolean {
        return !!this.tokenCodes
    }

    static noAuthentication(): AuthenticationModel {
        return new AuthenticationModel(null);
    }

    static authenticated(tokenCodes: TokenCodesModel): AuthenticationModel {
        return new AuthenticationModel(tokenCodes);
    }

}