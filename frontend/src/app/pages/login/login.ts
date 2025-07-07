import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
})
export class LoginComponent {
  email = '';
  password = '';

  onLogin() {
    // TODO: Implémente la logique de connexion (appels API)
    console.log('Tentative de connexion avec', this.email, this.password);
    
    // Exemple simplifié de vérification
    if (this.email && this.password) {
      alert(`Connexion réussie pour ${this.email}`);
      // Rediriger vers la page suivante après connexion
    } else {
      alert('Veuillez saisir un email et un mot de passe valides.');
    }
  }
}
