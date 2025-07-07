import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
})
export class RegisterComponent {
  onRegister() {
    // TODO: logique d'inscription à ajouter ici (appel API, validation, etc.)
    alert('Inscription réussie !');
  }
}
