import {BrowserModule} from "@angular/platform-browser";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {HttpClientModule} from "@angular/common/http";
import { CdkTableModule } from '@angular/cdk';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ApplicationRef, NgModule} from "@angular/core";
import {createInputTransfer, createNewHosts, removeNgStyles} from "@angularclass/hmr";
import {PreloadAllModules, RouterModule} from "@angular/router";
import { LocalStorageModule } from 'angular-2-local-storage';
import {
    MdButtonModule,
    MdCheckboxModule,
    MdDialogModule,
    MdInputModule,
    MdMenuModule,
    MdToolbarModule,
    MdTableModule,
    MdSortModule
} from "@angular/material";
/*
 * Platform and Environment providers/directives/pipes
 */

// modules
import {ServerModule} from './+server';
import {SecurityModule} from './+security';

import {ENV_PROVIDERS} from "./environment";
import {ROUTES} from "./app.routes";
// App is our top level component
import {AppComponent} from "./app.component";
import {APP_RESOLVER_PROVIDERS} from "./app.resolver";
import {AppState, InternalStateType} from "./app.service";
import {HomeComponent} from "./pages/home";
import {AboutComponent} from "./pages/about";
import {UsersComponent} from "./pages/users/users.component";
import {NoContentComponent} from "./pages/no-content";
import {XLargeDirective} from "./pages/home/x-large";
import {UserMenuComponent} from "./components/UserMenu";
import {SignInDialogComponent} from "./components/SignInDialog";
import {UserDetailsDialogComponent} from "./components/UserDetailsDialog";
import {SecurityService} from "./+security/security.service";
import {ResponseMappingService} from "./+security/responseMapping.service";
import {SecurityContextHolder} from "./+security/security.context";
import {UsersTableComponent} from "./components/UsersTable/UsersTable.component";
import {UserRepository} from "./repository/user.repository";

import "../styles/styles.scss";
import "../styles/headings.css";

// Application wide providers
const APP_PROVIDERS = [
    ...APP_RESOLVER_PROVIDERS,
    SecurityService,
    ResponseMappingService,
    SecurityContextHolder,
    UserRepository,
    AppState
];

type StoreType = {
    state: InternalStateType,
    restoreInputValues: () => void,
    disposeOldHosts: () => void
};

/**
 * `AppModule` is the main entry point into Angular2's bootstraping process
 */
@NgModule({
    bootstrap: [AppComponent],
    declarations: [
        AppComponent,
        AboutComponent,
        HomeComponent,
        UsersComponent,
        NoContentComponent,
        XLargeDirective,
        UserMenuComponent,
        SignInDialogComponent,
        UserDetailsDialogComponent,
        UsersTableComponent
    ],
    /**
     * Import Angular's modules.
     */
    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        HttpModule,
        HttpClientModule,
        ServerModule,
        SecurityModule,
        BrowserAnimationsModule,
        CdkTableModule,
        RouterModule.forRoot(ROUTES, {useHash: true, preloadingStrategy: PreloadAllModules}),
        MdInputModule,
        MdButtonModule,
        MdCheckboxModule,
        MdMenuModule,
        MdToolbarModule,
        MdDialogModule,
        MdTableModule,
        MdSortModule,
        LocalStorageModule.withConfig({
            prefix: 'timezones',
            storageType: 'localStorage'
        })
    ],

    entryComponents: [
        SignInDialogComponent,
        UserDetailsDialogComponent
    ],

    /**
     * Expose our Services and Providers into Angular's dependency injection.
     */
    providers: [
        ENV_PROVIDERS,
        APP_PROVIDERS
    ]
})
export class AppModule {

    constructor(public appRef: ApplicationRef,
                public appState: AppState) {
    }

    public hmrOnInit(store: StoreType) {
        if (!store || !store.state) {
            return;
        }
        console.log('HMR store', JSON.stringify(store, null, 2));
        /**
         * Set state
         */
        this.appState._state = store.state;
        /**
         * Set input values
         */
        if ('restoreInputValues' in store) {
            let restoreInputValues = store.restoreInputValues;
            setTimeout(restoreInputValues);
        }

        this.appRef.tick();
        delete store.state;
        delete store.restoreInputValues;
    }

    public hmrOnDestroy(store: StoreType) {
        const cmpLocation = this.appRef.components.map((cmp) => cmp.location.nativeElement);
        /**
         * Save state
         */
        const state = this.appState._state;
        store.state = state;
        /**
         * Recreate root elements
         */
        store.disposeOldHosts = createNewHosts(cmpLocation);
        /**
         * Save input values
         */
        store.restoreInputValues = createInputTransfer();
        /**
         * Remove styles
         */
        removeNgStyles();
    }

    public hmrAfterDestroy(store: StoreType) {
        /**
         * Display new elements
         */
        store.disposeOldHosts();
        delete store.disposeOldHosts;
    }

}
