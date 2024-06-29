import { Component, OnInit, Renderer2 } from '@angular/core';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';

@Component({
  selector: 'xp-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(private authService: AuthService, private renderer: Renderer2) {}

  getRole(): string {
    return this.authService.getRole();
  }
}
