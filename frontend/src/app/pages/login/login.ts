import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginDto, UserRole } from '../../models/user.models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
})
export class LoginComponent {
  email = '';
  password = '';
  isLoading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin() {
    if (!this.email || !this.password) {
      this.errorMessage = 'Veuillez saisir un email et un mot de passe valides.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const loginData: LoginDto = {
      email: this.email,
      motDePasse: this.password
    };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.success) {
          // Sauvegarder les données utilisateur
          this.authService.saveUserData(response);
          
          // Rediriger selon le rôle
          this.redirectUserByRole(response.role!);
        } else {
          this.errorMessage = response.message;
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Erreur de connexion. Veuillez réessayer.';
        console.error('Erreur de connexion:', error);
      }
    });
  }

  private redirectUserByRole(role: UserRole) {
    switch (role) {
      case UserRole.ALTERNANT:
        this.router.navigate(['/apprenti']);
        break;
      case UserRole.RECRUTEUR:
        this.router.navigate(['/recruteur']);
        break;
      case UserRole.ECOLE:
        // Redirection vers une page école (à créer)
        this.router.navigate(['/']);
        break;
      default:
        this.router.navigate(['/']);
    }
  }
}
