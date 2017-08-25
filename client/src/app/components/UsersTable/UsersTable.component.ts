import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {DataSource} from "@angular/cdk";
import {MdSort, MdDialog} from "@angular/material";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/observable/fromEvent";
import "rxjs/add/operator/map";
import "rxjs/add/operator/distinctUntilChanged";
import "rxjs/add/operator/debounceTime";
import {UserModel} from "../../models/User.model";
import {UserRepository} from "../../repository/user.repository";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {RoleModel} from "../../models/Role.model";
import {UserDetailsDialogComponent} from "../UserDetailsDialog/UserDetailsDialog.component";
import {Entity} from "../../models/hateoas/Entity.model";

@Component({
    selector: 'usersTable',
    styleUrls: ['UsersTable.css'],
    template: `
        
        <div class="example-container mat-elevation-z8">
            <div class="example-header">
                <md-input-container floatPlaceholder="never">
                    <input mdInput #filter placeholder="Filter users">
                </md-input-container>
            </div>
            <md-table #table [dataSource]="dataSource" mdSort>
                
                <ng-container cdkColumnDef="username">
                    <md-header-cell *cdkHeaderCellDef md-sort-header>Username</md-header-cell>
                    <md-cell *cdkCellDef="let row"> {{row.entity.username}} </md-cell>
                </ng-container>

                <ng-container cdkColumnDef="email">
                    <md-header-cell *cdkHeaderCellDef md-sort-header> E-mail </md-header-cell>
                    <md-cell *cdkCellDef="let row"> {{row.entity.email}} </md-cell>
                </ng-container>

                <ng-container cdkColumnDef="enabled">
                    <md-header-cell *cdkHeaderCellDef md-sort-header> Is enabled </md-header-cell>
                    <md-cell *cdkCellDef="let row"> {{row.entity.enabled ? 'Yes' : 'No'}} </md-cell>
                </ng-container>

                <ng-container cdkColumnDef="roles">
                    <md-header-cell *cdkHeaderCellDef md-sort-header> Roles </md-header-cell>
                    <md-cell *cdkCellDef="let row"> {{mapRoles(row.entity.roles)}} </md-cell>
                </ng-container>

                <ng-container cdkColumnDef="controls">
                    <md-header-cell *cdkHeaderCellDef md-sort-header> Actions </md-header-cell>
                    <md-cell *cdkCellDef="let row"> <button md-button (click)="edit(row)">Edit</button> </md-cell>
                </ng-container>

                <md-header-row *cdkHeaderRowDef="displayedColumns"></md-header-row>
                <md-row *cdkRowDef="let row; columns: displayedColumns;"></md-row>
            </md-table>
        </div>
        
    `,
})
export class UsersTableComponent implements OnInit {

    displayedColumns = ['username', 'email', 'enabled', 'roles', 'controls'];
    dataSource: UserTableDataSource | null;

    @ViewChild(MdSort)
    sort: MdSort;

    @ViewChild('filter')
    filter: ElementRef;

    constructor(private userRepository: UserRepository,
                private dialog: MdDialog) {

    }

    edit(userEntity) {
        const dialog = this.dialog.open(UserDetailsDialogComponent, {
            data: userEntity
        });
        dialog.afterClosed().subscribe(result => {
            console.log(`Dialog result: ${result}`);
        });
    }

    mapRoles(roles) {
        return roles.map((role) => RoleModel[role]).join(", ")
    }

    ngOnInit() {
        this.dataSource = new UserTableDataSource(this.userRepository, this.sort);
        Observable.fromEvent(this.filter.nativeElement, 'keyup')
            .debounceTime(150)
            .distinctUntilChanged()
            .subscribe(() => {
                if (!this.dataSource) { return; }
                this.dataSource.filter = this.filter.nativeElement.value;
            });
        this.dataSource.loadData();
    }

}

export class UserTableDataSource extends DataSource<any> {

    dataChange = new BehaviorSubject<Entity<UserModel>[]>([]);
    filterChange = new BehaviorSubject('');

    get filter(): string { return this.filterChange.value; }
    set filter(filter: string) { this.filterChange.next(filter); }

    constructor(private userRepository: UserRepository,
                private sort: MdSort) {
        super();
    }

    loadData() {
        this.userRepository.getAllUsers()
            .map((entityCollection) => entityCollection.entities)
            .subscribe((tab) => {
                this.dataChange.next(tab);
            });
    }

    connect(): Observable<Entity<UserModel>[]> {
        const displayDataChanges = [
            this.dataChange,
            this.sort.mdSortChange,
            this.filterChange,
        ];

        return Observable.merge(...displayDataChanges).map(() => {
            return this.getSortedData().filter((entity: Entity<UserModel>) => {
                const searchStr = (entity.entity.username + entity.entity.email).toLowerCase();
                return searchStr.indexOf(this.filter.toLowerCase()) != -1;
            });
        });
    }

    disconnect() {

    }

    getSortedData(): Entity<UserModel>[] {
        const data = this.dataChange.value.slice();

        if (!this.sort.active || this.sort.direction == '') {
            return data;
        }

        return data.sort((a, b) => {
            let propertyA: number | string = '';
            let propertyB: number | string = '';

            switch (this.sort.active) {
                case 'username':
                    [propertyA, propertyB] = [a.entity.username, b.entity.username];
                    break;
                case 'password':
                    [propertyA, propertyB] = [a.entity.email, b.entity.email];
                    break;
            }

            let valueA = isNaN(+propertyA) ? propertyA : +propertyA;
            let valueB = isNaN(+propertyB) ? propertyB : +propertyB;

            return (valueA < valueB ? -1 : 1) * (this.sort.direction == 'asc' ? 1 : -1);
        });
    }

}
