import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { LoginService } from '../login.service';
import { TwoFactorAuthenticationStatus } from '../model/TwoFactorAuthenticationStatus.model';
import { NotifierService } from 'angular-notifier';
import { TwoFactorAuthenticationSetupCompletion } from '../model/TwoFactorAuthenticationSetupCompletion.model';

@Component({
  selector: 'xp-toggle-two-factor-authentication',
  templateUrl: './toggle-two-factor-authentication.component.html',
  styleUrls: ['./toggle-two-factor-authentication.component.css']
})
export class ToggleTwoFactorAuthenticationComponent {
  isEnabled: boolean = false;
  isSetupSuccessfull: boolean = false;
  qrCode: string = "";
  code: TwoFactorAuthenticationSetupCompletion = new TwoFactorAuthenticationSetupCompletion();

  constructor(
    private notifier: NotifierService,
    public dialogRef: MatDialogRef<ToggleTwoFactorAuthenticationComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: any,
    private loginService: LoginService 
  ) {}

  ngOnInit(): void {
    this.check2FactorAuthenticationStatus();
  }

  check2FactorAuthenticationStatus() {
    this.loginService.check2FactorAuthentication().subscribe(
      (response: TwoFactorAuthenticationStatus) => {
        this.isEnabled = response.enabled;
      },
      (error: any) => {
        this.notifier.notify('error', error.error.error);
        this.close();
      }
    );
  }

  toggle() {
    if(!this.isEnabled) {
      this.loginService.setup2FactorAuthentication().subscribe(
        (response: any) => {
          this.isSetupSuccessfull = true;
          this.qrCode = response.qrCode;
          this.notifier.notify('success', 'Successfully initialized two factor authentication setup. Scan the QR code in your Google Authenticator app and complete the setup by typing the code from the app below.');
        },
        (error: any) => {
          this.notifier.notify('error', error.error.error);
        }
      );
    }
    else {
      this.loginService.disable2FactorAuthentication().subscribe(
        (response: any) => {
          this.notifier.notify('success', 'Successfully disabled two factor authentication.');
          this.close();
        },
        (error: any) => {
          this.notifier.notify('error', error.error.error);
        }
      );
    }
  }

  enable() {
    this.loginService.enable2FactorAuthentication(this.code).subscribe(
      (response: any) => {
        this.notifier.notify('success', 'Successfully enabled two factor authentication.');
        this.close();
      },
      (error: any) => {
        this.notifier.notify('error', error.error.error);
      }
    );
  }

  close() {
    this.dialogRef.close();
  }
}
