import {Component, ElementRef, ViewChild} from "@angular/core";
import {DataSource} from "@angular/cdk";
import {MdSort} from "@angular/material";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {UserModel} from "../../models/User.model";
import {UserRepository} from "../../repository/user.repository";

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
export class UsersTableComponent {

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
    }

}

export class UserTableDataSource extends DataSource<any> {

    constructor(private userRepository: UserRepository,
                private sort: MdSort) {
        super();
    }

    connect(): Observable<UserModel[]> {
        const displayDataChanges = [
            this.userRepository.dataChange,
            this.sort.mdSortChange,
        ];

        return Observable.merge(...displayDataChanges).map(() => {
            return this.getSortedData();
        });
    }

    disconnect() {
    }

    getSortedData(): UserModel[] {
        const data = this.userRepository.data.slice();

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
