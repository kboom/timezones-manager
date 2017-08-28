import {Component, EventEmitter, Input, Output} from "@angular/core";
import {MdDialog} from "@angular/material";
import {EditTimezoneDialogComponent} from "../EditTimezoneDialog/EditTimezoneDialog.component";

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
                        {{ (timezone.differenceToGMT >= 0 ? "+" : "-") + timezone.differenceToGMT }}
                    </md-card-subtitle>
                </md-card-title-group>
            </md-card-header>
            <md-card-content>
                Nice analog-live clock will be placed here
            </md-card-content>
            <md-card-actions>
                <button md-button (click)="onEdit.emit()">Edit</button>
                <button md-button (click)="onDelete.emit()">Delete</button>
            </md-card-actions>
        </md-card>

    `
})
export class TimezoneComponent {

    @Input()
    private timezone: Timezone;

    @Output()
    private onEdit: EventEmitter<any> = new EventEmitter();

    @Output()
    private onDelete: EventEmitter<any> = new EventEmitter();

}