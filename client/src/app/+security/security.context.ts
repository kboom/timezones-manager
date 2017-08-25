import {Injectable} from "@angular/core";
import {AuthenticationModel} from "../models/Authentication.model";

@Injectable()
export class SecurityContextHolder {

    authentication: AuthenticationModel = AuthenticationModel.noAuthentication();

}