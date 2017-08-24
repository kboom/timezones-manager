import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home';
import { AboutComponent } from './pages/about';
import { UsersComponent } from './pages/users';
import { NoContentComponent } from './pages/no-content';

import { DataResolver } from './app.resolver';

export const ROUTES: Routes = [
  { path: '',      component: HomeComponent },
  { path: 'home',  component: HomeComponent },
  { path: 'users',  component: UsersComponent },
  { path: 'about', component: AboutComponent },
  { path: 'detail', loadChildren: './modules/+detail#DetailModule'},
  { path: 'barrel', loadChildren: './modules/+barrel#BarrelModule'},
  { path: '**',    component: NoContentComponent },
];
