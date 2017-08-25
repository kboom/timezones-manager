import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {DataSource} from "@angular/cdk";
import {MdSort} from "@angular/material";
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
                    <md-cell *cdkCellDef="let row"> {{row.username}} </md-cell>
                </ng-container>

                <ng-container cdkColumnDef="email">
                    <md-header-cell *cdkHeaderCellDef md-sort-header> E-mail </md-header-cell>
                    <md-cell *cdkCellDef="let row"> {{row.email}} </md-cell>
                </ng-container>

                <md-header-row *cdkHeaderRowDef="displayedColumns"></md-header-row>
                <md-row *cdkRowDef="let row; columns: displayedColumns;"></md-row>
            </md-table>
        </div>
        
    `,
})
export class UsersTableComponent implements OnInit {

    displayedColumns = ['username', 'email'];
    dataSource: UserTableDataSource | null;

    @ViewChild(MdSort)
    sort: MdSort;

    @ViewChild('filter')
    filter: ElementRef;

    constructor(private userRepository: UserRepository) {

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

    dataChange = new BehaviorSubject<UserModel[]>([]);
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

    connect(): Observable<UserModel[]> {
        const displayDataChanges = [
            this.dataChange,
            this.sort.mdSortChange,
            this.filterChange,
        ];

        return Observable.merge(...displayDataChanges).map(() => {
            return this.getSortedData().filter((item: UserModel) => {
                const searchStr = (item.username + item.email).toLowerCase();
                return searchStr.indexOf(this.filter.toLowerCase()) != -1;
            });
        });
    }

    disconnect() {

    }

    getSortedData(): UserModel[] {
        const data = this.dataChange.value.slice();

        if (!this.sort.active || this.sort.direction == '') {
            return data;
        }

        return data.sort((a, b) => {
            let propertyA: number | string = '';
            let propertyB: number | string = '';

            switch (this.sort.active) {
                case 'username':
                    [propertyA, propertyB] = [a.username, b.username];
                    break;
                case 'password':
                    [propertyA, propertyB] = [a.email, b.email];
                    break;
            }

            let valueA = isNaN(+propertyA) ? propertyA : +propertyA;
            let valueB = isNaN(+propertyB) ? propertyB : +propertyB;

            return (valueA < valueB ? -1 : 1) * (this.sort.direction == 'asc' ? 1 : -1);
        });
    }

}
