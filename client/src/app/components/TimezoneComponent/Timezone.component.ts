import {Component, Input} from "@angular/core";

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
                    <md-card-subtitle><strong>{{ timezone.locationName }}</strong>, GMT {{ (timezone.differenceToGMT >= 0 ? "+" : "-") + timezone.differenceToGMT }}</md-card-subtitle>
                </md-card-title-group>
            </md-card-header>
            <md-card-content>
                Nice analog-live clock will be placed here
            </md-card-content>
            <md-card-actions>
                <button md-button>Edit</button>
            </md-card-actions>
        </md-card>

    `
})
export class TimezoneComponent {

    @Input()
    private timezone: Timezone

}