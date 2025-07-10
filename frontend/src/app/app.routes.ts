import { Routes } from '@angular/router';
import { ApprentiDashboardComponent } from './pages/apprenti-dashboard/apprenti-dashboard';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password';
import { HomeComponent } from './pages/home/home';
import { LoginComponent } from './pages/login/login';
import { RecruteurDashboardComponent } from './pages/recruteur-dashboard/recruteur-dashboard';
import { RegisterComponent } from './pages/register/register';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'login', component: LoginComponent  },
    { path: 'forgot-password', component: ForgotPasswordComponent },
    { path: 'apprenti', component: ApprentiDashboardComponent },
    { path: 'recruteur', component: RecruteurDashboardComponent },   
    { path: 'reset-password', loadComponent: () => import('./pages/reset-password/reset-password').then(m => m.ResetPasswordComponent) },

    { path: '**', redirectTo: '' },
];
