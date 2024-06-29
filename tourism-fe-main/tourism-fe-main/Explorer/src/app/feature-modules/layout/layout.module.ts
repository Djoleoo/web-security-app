import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { NavbarComponent } from './navbar/navbar.component';
import { MaterialModule } from 'src/app/infrastructure/material/material.module';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { RegistrationComponent } from './registration/registration.component';
import { FormsModule } from '@angular/forms';
import { AccountActivationComponent } from './account-activation/account-activation.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ClientRegistrationRequestRejectionComponent } from './client-registration-request-rejection/client-registration-request-rejection.component';
import { ClientRegistrationRequestsComponent } from './client-registration-requests/client-registration-requests.component';
import { ClientRegistrationRequestAcceptanceComponent } from './client-registration-request-acceptance/client-registration-request-acceptance.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { UnauthorizedAccessComponent } from './unauthorized-access/unauthorized-access.component';
import { UnauthenticatedAccessComponent } from './unauthenticated-access/unauthenticated-access.component';
import { RequestPasswordlessLoginComponent } from './request-passwordless-login/request-passwordless-login.component';
import { PasswordlessLoginComponent } from './passwordless-login/passwordless-login.component';
import { AdminUpdateComponent } from './admin-update/admin-update.component';
import { ClientUpdateComponent } from './client-update/client-update.component';
import { ClientEmployeeListComponent } from './client-employee-list/client-employee-list.component';
import { AdvertisementRequestComponent } from './advertisement-request/advertisement-request.component';
import { AdvertisementRequestListComponent } from './advertisement-request-list/advertisement-request-list.component';
import { AdvertisementListComponent } from './advertisement-list/advertisement-list.component';
import { CreateUserComponent } from './create-user/create-user.component';
import { EmployeeUpdateComponent } from './employee-update/employee-update.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { RefreshTokenComponent } from './refresh-token/refresh-token.component';
import { MyAdvertisementsComponent } from './my-advertisements/my-advertisements.component';
import { TwoFactorAuthenticationComponent } from './two-factor-authentication/two-factor-authentication.component';
import { ToggleTwoFactorAuthenticationComponent } from './toggle-two-factor-authentication/toggle-two-factor-authentication.component';
import { QRCodeModule } from 'angularx-qrcode';
import { TwoFactorAuthenticationPasswordlessComponent } from './two-factor-authentication-passwordless/two-factor-authentication-passwordless.component';
import { RecaptchaModule, RecaptchaFormsModule } from 'ng-recaptcha';
import { ClientProfileComponent } from './client-profile/client-profile.component';
import { AccountDeletionComponent } from './account-deletion/account-deletion.component';
import {
  SocialLoginModule,
  SocialAuthServiceConfig,
} from '@abacritt/angularx-social-login';
import { GoogleLoginProvider } from '@abacritt/angularx-social-login';
import {  GoogleSigninButtonModule } from '@abacritt/angularx-social-login';
import { RequestResetPasswordComponent } from './request-reset-password/request-reset-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';

@NgModule({
  declarations: [
    HomeComponent,
    NavbarComponent,
    HeaderComponent,
    RegistrationComponent,
    AccountActivationComponent,
    ClientRegistrationRequestRejectionComponent,
    ClientRegistrationRequestsComponent,
    ClientRegistrationRequestAcceptanceComponent,
    LoginComponent,
    LogoutComponent,
    UnauthorizedAccessComponent,
    UnauthenticatedAccessComponent,
    RequestPasswordlessLoginComponent,
    PasswordlessLoginComponent,
    AdminUpdateComponent,
    ClientUpdateComponent,
    ClientEmployeeListComponent,
    AdvertisementRequestComponent,
    AdvertisementRequestListComponent,
    AdvertisementListComponent,
    CreateUserComponent,
    EmployeeUpdateComponent,
    ChangePasswordComponent,
    RefreshTokenComponent,
    MyAdvertisementsComponent,
    TwoFactorAuthenticationComponent,
    ToggleTwoFactorAuthenticationComponent,
    TwoFactorAuthenticationPasswordlessComponent,
    ClientProfileComponent,
    AccountDeletionComponent,
    RequestResetPasswordComponent,
    ResetPasswordComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule,
    FormsModule,
    MatDialogModule,
    QRCodeModule,
    RecaptchaModule,
    RecaptchaFormsModule,
    SocialLoginModule,
    GoogleSigninButtonModule
  ],
  exports: [
    NavbarComponent,
    HomeComponent,
    HeaderComponent
  ],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider('677445070186-1pn3735tq8ct2adfdgq9gfnn2b52o18c.apps.googleusercontent.com'),
          },
        ],
      } as SocialAuthServiceConfig,
    },
  ],
})
export class LayoutModule { }
