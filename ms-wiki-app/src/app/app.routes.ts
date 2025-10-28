import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./core/core.module').then((m) => m.CoreModule),
  },
  {
    path: '**',
    redirectTo: 'auth',
  },
]; // path: 'auth', path:'**': redirect: 'auth'
