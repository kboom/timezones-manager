import {Component, Input} from "@angular/core";
import {FormControl} from "@angular/forms";
import {ValidationService} from "../../services/validation.service";

@Component({
    selector: 'control-messages',
    template: `<div *ngIf="errorMessage !== null" style="padding-bottom: 20px" class="mat-body-strong">{{errorMessage}}</div>`
})
export class ControlMessagesComponent {

    @Input()
    control: FormControl;

    get errorMessage() {
        for (let propertyName in this.control.errors) {
            if (this.control.errors.hasOwnProperty(propertyName) && this.control.touched) {
                return ValidationService.getValidatorErrorMessage(propertyName, this.control.errors[propertyName]);
            }
        }
        return null;
    }

}
