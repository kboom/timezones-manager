import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import * as moment from 'moment';
import {Observable} from "rxjs/Observable";

interface Timezone {
    name: string
    differenceToGMT: number
    city: string
}

@Component({
    selector: 'timezone',
    styleUrls: ['Timezone.scss'],
    template: `

        <md-card>
            <md-card-header>
                <md-card-title-group>
                    <md-card-title>{{ timezone.name }}</md-card-title>
                    <md-card-subtitle><strong>{{ timezone.locationName }}</strong>, GMT
                        {{ (timezone.differenceToGMT >= 0 ? "+" : "") + timezone.differenceToGMT }}
                    </md-card-subtitle>
                </md-card-title-group>
            </md-card-header>
            <md-card-content>
                <div class="timezone__time">{{ time$ | async | amDateFormat: 'HH:mm:ss' }}</div>
            </md-card-content>
            <md-card-actions>
                <button md-button (click)="onEdit.emit()">Edit</button>
                <button md-button (click)="onDelete.emit()">Delete</button>
            </md-card-actions>
        </md-card>

    `
})
export class TimezoneComponent implements OnInit {

    private time$ = Observable.interval(1000)
        .map((minute) => moment().add(this.timezone.differenceToGMT, "hours").utc(false));

    @Input()
    private timezone: Timezone;

    @Output()
    private onEdit: EventEmitter<any> = new EventEmitter();

    @Output()
    private onDelete: EventEmitter<any> = new EventEmitter();

    ngOnInit(): void {
        const dateTime = moment();

    }

}