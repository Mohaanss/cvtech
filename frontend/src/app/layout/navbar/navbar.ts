import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.html',
  standalone : true,
  styleUrls: [],
    imports: [CommonModule,RouterModule],

})
export class NavbarComponent {
  open = false;
  toggleMenu() {
    this.open = !this.open;
  }
}
