import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from './layout/footer/footer';
import { NavbarComponent } from './layout/navbar/navbar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,FooterComponent,NavbarComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'frontend';
}
