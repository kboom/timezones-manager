import {Routes} from "@angular/router";
import {HomeComponent} from "./pages/home";
import {AboutComponent} from "./pages/about";
import {UsersComponent} from "./pages/users";
import {NoContentComponent} from "./pages/no-content";
import {RegistrationComponent} from "./pages/registration/registration.component";
import {TIMEZONES_PAGE_ROUTE} from "./pages/timezones/timezones.page";

export const ROUTES: Routes = [
    {path: '', component: HomeComponent},
    {path: 'home', component: HomeComponent},
    {path: 'users', component: UsersComponent},
    TIMEZONES_PAGE_ROUTE,
    {path: 'about', component: AboutComponent},
    {path: 'register', component: RegistrationComponent},
    {path: '**', component: NoContentComponent},
];
