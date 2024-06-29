import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from 'src/app/feature-modules/layout/home/home.component';
import { RegistrationComponent } from 'src/app/feature-modules/layout/registration/registration.component';
import { AccountActivationComponent } from 'src/app/feature-modules/layout/account-activation/account-activation.component';
import { ClientRegistrationRequestsComponent } from 'src/app/feature-modules/layout/client-registration-requests/client-registration-requests.component';
import { LoginComponent } from 'src/app/feature-modules/layout/login/login.component';
import { AdministratorAuthGuard } from '../auth/guard/administrator-auth-guard';
import { ClientAuthGuard } from '../auth/guard/client-auth-guard';
import { EmployeeAuthGuard } from '../auth/guard/employee-auth-guard';
import { AuthGuard } from '../auth/guard/auth-guard';
import { ReverseAuthGuard } from '../auth/guard/reverse-auth-guard';
import { UnauthenticatedAccessComponent } from 'src/app/feature-modules/layout/unauthenticated-access/unauthenticated-access.component';
import { UnauthorizedAccessComponent } from 'src/app/feature-modules/layout/unauthorized-access/unauthorized-access.component';
import { RequestPasswordlessLoginComponent } from 'src/app/feature-modules/layout/request-passwordless-login/request-passwordless-login.component';
import { PasswordlessLoginComponent } from 'src/app/feature-modules/layout/passwordless-login/passwordless-login.component';
import { AdminUpdateComponent } from 'src/app/feature-modules/layout/admin-update/admin-update.component';
import { ClientUpdateComponent } from 'src/app/feature-modules/layout/client-update/client-update.component';
import { ClientEmployeeListComponent } from 'src/app/feature-modules/layout/client-employee-list/client-employee-list.component';
import { AdvertisementRequestComponent } from 'src/app/feature-modules/layout/advertisement-request/advertisement-request.component';
import { AdvertisementRequestListComponent } from 'src/app/feature-modules/layout/advertisement-request-list/advertisement-request-list.component';
import { AdvertisementListComponent } from 'src/app/feature-modules/layout/advertisement-list/advertisement-list.component';
import { EmployeeUpdateComponent } from 'src/app/feature-modules/layout/employee-update/employee-update.component';
import { CreateUserComponent } from 'src/app/feature-modules/layout/create-user/create-user.component';
import { ChangePasswordComponent } from 'src/app/feature-modules/layout/change-password/change-password.component';
import { RefreshTokenComponent } from 'src/app/feature-modules/layout/refresh-token/refresh-token.component';
import { MyAdvertisementsComponent } from 'src/app/feature-modules/layout/my-advertisements/my-advertisements.component';
import { FirstLoginGuard } from '../auth/guard/first-login-guard';
import { ClientProfileComponent } from 'src/app/feature-modules/layout/client-profile/client-profile.component';
import { RequestResetPasswordComponent } from 'src/app/feature-modules/layout/request-reset-password/request-reset-password.component';
import { ResetPasswordComponent } from 'src/app/feature-modules/layout/reset-password/reset-password.component';

const routes: Routes = [
  {
    path: '', 
    component: HomeComponent
  },
  {
    path: 'home', 
    component: HomeComponent,
    canActivate:[FirstLoginGuard]
  },
  {
    path: 'sign-up', 
    component: RegistrationComponent, 
    canActivate: [ReverseAuthGuard]
  },
  {
    path: 'sign-in', 
    component: LoginComponent, 
    canActivate: [ReverseAuthGuard]
  },
  {
    path: 'request-passwordless-sign-in', 
    component: RequestPasswordlessLoginComponent, 
    canActivate: [ReverseAuthGuard]
  },
  {
    path: 'passwordless-sign-in', 
    component: PasswordlessLoginComponent
  },
  {
    path: 'account-activation', 
    component: AccountActivationComponent, 
    canActivate: [ReverseAuthGuard]
  },
  {
    path: 'unauthenticated-access', 
    component: UnauthenticatedAccessComponent
  },
  {
    path: 'unauthorized-access', 
    component: UnauthorizedAccessComponent
  },
  {
    path: 'client-registration-requests', 
    component: ClientRegistrationRequestsComponent, 
    canActivate: [AdministratorAuthGuard,FirstLoginGuard]
  },
  {
    path: 'admin-update',
    component: AdminUpdateComponent,
    canActivate:[AdministratorAuthGuard,FirstLoginGuard]
  },
  {
    path:'client-update',
    component:ClientUpdateComponent,
    canActivate:[ClientAuthGuard]
  },
  {
    path:'client-employee-list',
    component:ClientEmployeeListComponent,
    canActivate:[AdministratorAuthGuard,FirstLoginGuard]
  },
  {
    path: 'advertisement-request',
    component: AdvertisementRequestComponent,
    canActivate: [ClientAuthGuard]
  },
  {
    path: 'advertisement-request-list',
    component: AdvertisementRequestListComponent,
    canActivate: [EmployeeAuthGuard,FirstLoginGuard]
  },
  {
    path: 'advertisements',
    component: AdvertisementListComponent,
    canActivate: [EmployeeAuthGuard,FirstLoginGuard]
  },
  {
    path: 'employee-update',
    component: EmployeeUpdateComponent,
    canActivate:[EmployeeAuthGuard,FirstLoginGuard]
  },
  {
    path: 'user-creation',
    component: CreateUserComponent,
    canActivate:[AdministratorAuthGuard,FirstLoginGuard]
  },
  {
    path: 'change-password',
    component:ChangePasswordComponent
  },
  {
    path: 'refresh-token',
    component:RefreshTokenComponent,
    canActivate:[FirstLoginGuard]
  },
  {
    path: 'my-advertisements',
    component:  MyAdvertisementsComponent,
    canActivate:[FirstLoginGuard]
  },
  {
    path:'client-profile',
    component:ClientProfileComponent,
    canActivate:[ClientAuthGuard]
  },
  {
    path:'request-reset-password',
    component: RequestResetPasswordComponent,
    canActivate:[ReverseAuthGuard]
  },
  {
    path:'reset-password',
    component: ResetPasswordComponent,
    canActivate:[ReverseAuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
