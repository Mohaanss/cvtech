import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './forgot-password.html',
})
export class ForgotPasswordComponent {
  email = '';
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onForgotPassword() {
    if (!this.email) {
      this.errorMessage = 'Veuillez saisir une adresse email valide.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.forgotPassword(this.email).subscribe({
      next: (response: any) => {
        this.isLoading = false;
        if (response.success) {
          this.successMessage = 'Un email de réinitialisation a été envoyé à votre adresse email.';
          this.email = '';
        } else {
          this.errorMessage = response.message || 'Une erreur est survenue.';
        }
      },
      error: (error: any) => {
        this.isLoading = false;
        this.errorMessage = 'Erreur lors de l\'envoi de l\'email. Veuillez réessayer.';
        console.error('Erreur mot de passe oublié:', error);
      }
    });
  }
} 