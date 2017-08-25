import {BehaviorSubject} from "rxjs/BehaviorSubject";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {Injectable} from "@angular/core";
import {UserModel} from "../models/User.model";

@Injectable()
export class UserRepository {

    dataChange: BehaviorSubject<UserModel[]> = new BehaviorSubject<UserModel[]>([]);

    constructor() {
        const copiedData = this.data.slice();
        copiedData.push(new UserModel("user1","mail4"));
        copiedData.push(new UserModel("user2","mail3"));
        copiedData.push(new UserModel("user3","mail2"));
        copiedData.push(new UserModel("user4","mail1"));
        this.dataChange.next(copiedData);
    }

    get data(): UserModel[] {
        return this.dataChange.value;
    }

}
