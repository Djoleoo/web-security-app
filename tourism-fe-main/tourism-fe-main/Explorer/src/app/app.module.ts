import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './infrastructure/routing/app-routing.module';
import { AppComponent } from './app.component';
import { LayoutModule } from './feature-modules/layout/layout.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './infrastructure/material/material.module';
import { AuthModule } from './infrastructure/auth/auth.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { JwtInterceptor } from './infrastructure/auth/jwt/jwt.interceptor';
import { NotifierModule, NotifierOptions } from 'angular-notifier';
import { FormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { QRCodeModule } from 'angularx-qrcode';
import { RecaptchaModule, RecaptchaFormsModule } from 'ng-recaptcha';
import {
  SocialLoginModule,
  SocialAuthServiceConfig,
} from '@abacritt/angularx-social-login';
import { GoogleLoginProvider } from '@abacritt/angularx-social-login';

const notifierConfig: NotifierOptions = {
  position: {
      horizontal: {
          position: "right",
          distance: 12,
      },
      vertical: {
          position: "bottom",
          distance: 12,
          gap: 10,
      },
  },
  theme: "material",
  behaviour: {
      autoHide: 5000,
      onClick: false,
      onMouseover: "pauseAutoHide",
      showDismissButton: true,
      stacking: 4,
  },
  animations: {
      enabled: true,
      show: {
          preset: "slide",
          speed: 300,
          easing: "ease",
      },
      hide: {
          preset: "fade",
          speed: 300,
          easing: "ease",
          offset: 50,
      },
      shift: {
          speed: 300,
          easing: "ease",
      },
      overlap: 150,
  },
};

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    LayoutModule,
    BrowserAnimationsModule,
    MaterialModule,
    AuthModule,
    HttpClientModule,
    NotifierModule.withConfig(notifierConfig),
    FormsModule,
    MatDialogModule,
    QRCodeModule,
    RecaptchaModule,
    RecaptchaFormsModule,
    SocialLoginModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    },
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
  bootstrap: [AppComponent]
})
export class AppModule { }
