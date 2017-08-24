import {Injectable} from "@angular/core";
import { Response } from "@angular/http";
import {AuthenticationModel} from "../models/Authentication.model";
import {TokenCodesModel} from "../models/Token.model";

@Injectable()
export class ResponseMappingService {

    mapIntoTokenCodes(object): TokenCodesModel {
console.log("resonse");
        return new TokenCodesModel("abc")
    }

}
