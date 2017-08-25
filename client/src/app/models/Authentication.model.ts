import * as JWT from "jwt-decode";
import {TokenCodesModel} from "./Token.model";
import {RoleModel} from "./Role.model";

export class AuthenticationModel {

    readonly details;

    constructor(readonly tokenCodes: TokenCodesModel) {
        this.tokenCodes = tokenCodes;
        if (!!this.tokenCodes) {
            this.details = JWT(this.tokenCodes.accessToken);
        }
    }

    getUsername() {
        console.log(JSON.stringify(this.details));
        return this.details.username;
    }

    hasAnyRole(...role: RoleModel[]) {
        if (this.details) {
            return role.map((roleId) => RoleModel[roleId])
                .some(v => this.details.authorities.includes(v))
        } else {
            return false;
        }
    }

    isAuthenticated(): Boolean {
        return !!this.tokenCodes
    }

    static noAuthentication(): AuthenticationModel {
        return new AuthenticationModel(null);
    }

    static authenticated(tokenCodes: TokenCodesModel): AuthenticationModel {
        return new AuthenticationModel(tokenCodes);
    }

}