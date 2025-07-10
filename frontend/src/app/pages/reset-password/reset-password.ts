import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reset-password.html',
})
export class ResetPasswordComponent implements OnInit {
  token: string = '';
  tokenValid = false;
  tokenChecked = false;
  newPassword = '';
  confirmPassword = '';
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'] || '';
      if (this.token) {
        this.validateToken();
      } else {
        this.tokenChecked = true;
        this.tokenValid = false;
      }
    });
  }

  validateToken() {
    this.http.get<any>(`http://localhost:8081/api/password/validate-token/${this.token}`).subscribe({
      next: (res) => {
        this.tokenValid = res.valid;
        this.tokenChecked = true;
      },
      error: () => {
        this.tokenValid = false;
        this.tokenChecked = true;
      }
    });
  }

  onSubmit() {
    if (!this.newPassword || !this.confirmPassword) {
      this.errorMessage = 'Veuillez remplir tous les champs.';
      return;
    }
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Les mots de passe ne correspondent pas.';
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.http.post<any>('http://localhost:8081/api/password/reset', {
      token: this.token,
      newPassword: this.newPassword,
      confirmPassword: this.confirmPassword
    }).subscribe({
      next: (res) => {
        if (res.success) {
          this.successMessage = 'Votre mot de passe a été réinitialisé avec succès.';
          setTimeout(() => this.router.navigate(['/login']), 2000);
        } else {
          this.errorMessage = res.message || 'Erreur lors de la réinitialisation.';
        }
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la réinitialisation.';
        this.isLoading = false;
      }
    });
  }
} 