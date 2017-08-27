import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/observable/zip";
import "rxjs/add/operator/map";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {EntityCollectionModel} from "../models/hateoas/EntityCollection.model";
import {extend, omit, transform} from "lodash-es";
import {TimezoneModel} from "src/app/models/Timezone.model";
import {TimezoneFactory} from "../models/factory/index";

const getAllTimezonesUrl = "http://localhost:8080/api/timezones?projection=withDetails";

@Injectable()
export class TimezonesRepository {

    constructor(private http: HttpClient,
                private timezoneFactory: TimezoneFactory) {

    }

    public getAllTimezones(): Observable<EntityCollectionModel<TimezoneModel>> {
        return this.http.get(getAllTimezonesUrl)
            .map((body: any) => new EntityCollectionModel('timezones', body, this.timezoneFactory))
            .catch(() => Observable.throw("Could not get timezones"));
    }

}
