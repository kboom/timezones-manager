import {Component, Injectable, OnInit} from "@angular/core";
import {Title} from "./title";
import {XLargeDirective} from "./x-large";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {ActivatedRoute, ActivatedRouteSnapshot, CanActivate, Resolve, RouterStateSnapshot} from "@angular/router";
import {SecurityContextHolder} from "../../+security/security.context";
import {RoleModel} from "../../models/Role.model";
import {TimezonesRepository} from "../../repository/timezones.repository";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {Entity} from "../../models/hateoas/Entity.model";
import {TimezoneModel} from "../../models/Timezone.model";
import {EditTimezoneDialogComponent} from "../../components/EditTimezoneDialog/EditTimezoneDialog.component";
import {MdDialog} from "@angular/material";
import {CreateTimezoneDialogComponent} from "../../components/CreateTimezoneDialog/CreateTimezoneDialog.component";

@Component({
    selector: 'timezonesPage',
    providers: [],
    styleUrls: ['./timezones.page.scss'],
    template: `

        <div class="timezones__container" fxLayout='row wrap' fxLayoutAlign='center start' fxLayoutGap="20px">

            <timezone class="timezones__timezone" fxFlex="nogrow" [timezone]="timezone.entity"
                      class="timezones__timezone"
                      *ngFor="let timezone of timezones$ | async" (onEdit)="editTimezone(timezone)"></timezone>

        </div>

    `
})
export class TimezonesPage implements OnInit {

    private timezones$ = new BehaviorSubject<Entity<TimezoneModel>[]>([]);

    constructor(private route: ActivatedRoute, private dialog: MdDialog) {
    }

    ngOnInit(): void {
        this.route.data.map((data) => data.timezones).subscribe((timezones) => {
            this.timezones$.next(timezones.entities);
        })
    }

    editTimezone(timezoneEntity) {
        this.dialog.open(EditTimezoneDialogComponent, {
            data: timezoneEntity
        }).afterClosed()
            .subscribe(result => {

            });
    }

}

@Component({
    selector: 'timezones-toolbar',
    styleUrls: ["timezones.page.scss"],
    providers: [],
    template: `

        <md-toolbar-row class="timezones-navbar">
            <div style="width: 100%" fxLayout="row" fxLayoutAlign="space-between center" fxLayoutGap="20px">
                <md-input-container class="timezones-navbar__element" fxFlex="grow">
                    <input mdInput placeholder="Filter">
                </md-input-container>
                <button md-raised-button class="timezones-navbar__element mat-primary" (click)="createTimezone()">Create</button>
            </div>
        </md-toolbar-row>

    `
})
export class TimezonesToolbarComponent {

    constructor(private dialog: MdDialog) {}

    createTimezone(timezoneEntity) {
        this.dialog.open(CreateTimezoneDialogComponent).afterClosed()
            .subscribe(result => {

            });
    }

}

@Injectable()
export class TimezonesResolver implements Resolve<any> {

    constructor(private timezoneRepository: TimezonesRepository) {
    }

    public resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return this.timezoneRepository.getAllTimezones();
    }

}

@Injectable()
export class CanActivateTimezones implements CanActivate {

    constructor(private authContextHolder: SecurityContextHolder) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.authContextHolder.getAuthentication().hasAnyRole(RoleModel.ROLE_USER, RoleModel.ROLE_ADMIN)
    }

}

export const TIMEZONES_PAGE_ROUTE = {
    path: 'timezones',
    component: TimezonesPage,
    resolve: {
        timezones: TimezonesResolver
    },
    data: {
        toolbar: TimezonesToolbarComponent
    },
    canActivate: [CanActivateTimezones]
};
