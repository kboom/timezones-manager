import {Component, OnDestroy, OnInit} from "@angular/core";
import {Title} from "./title";
import {XLargeDirective} from "./x-large";
import "rxjs/add/operator/startWith";
import "rxjs/add/observable/merge";
import "rxjs/add/operator/map";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs/Subscription";

@Component({
    selector: 'registrationPage',
    template: `

        <div>Your account has been verified.
            <button md-button>Sign in</button>
        </div>

    `
})
export class RegistrationComponent implements OnInit, OnDestroy {

    queryParams$$: Subscription;

    constructor(private route: ActivatedRoute,) {
    }

    public ngOnInit() {
        this.queryParams$$ = this.route.queryParams
            .subscribe(params => {
                alert(params['confirmation'])
            });
    }

    ngOnDestroy() {
        this.queryParams$$.unsubscribe();
    }

}
