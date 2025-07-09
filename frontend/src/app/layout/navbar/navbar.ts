import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserRole } from '../../models/user.models';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.html',
  standalone : true,
  styleUrls: [],
  imports: [CommonModule, RouterModule],
})
export class NavbarComponent {
  open = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  toggleMenu() {
    this.open = !this.open;
  }

  /**
   * Vérifie si l'utilisateur est connecté
   */
  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  /**
   * Récupère l'utilisateur actuel
   */
  getCurrentUser(): any {
    return this.authService.getCurrentUser();
  }

  /**
   * Déconnexion de l'utilisateur
   */
  logout(): void {
    if (confirm('Êtes-vous sûr de vouloir vous déconnecter ?')) {
      console.log('🚪 Déconnexion en cours...');
      this.authService.logout();
      this.open = false; // Fermer le menu mobile
      this.router.navigate(['/']);
      console.log('✅ Déconnexion terminée, redirection vers l\'accueil');
    }
  }

  /**
   * Obtient l'URL du dashboard selon le rôle
   */
  getDashboardUrl(): string {
    const currentUser = this.getCurrentUser();
    if (!currentUser) return '/';
    
    switch (currentUser.role) {
      case UserRole.ALTERNANT:
        return '/apprenti';
      case UserRole.RECRUTEUR:
        return '/recruteur';
      case UserRole.ECOLE:
        return '/'; // À adapter quand vous aurez un dashboard école
      default:
        return '/';
    }
  }

  /**
   * Obtient le nom à afficher pour l'utilisateur
   */
  getUserDisplayName(): string {
    const currentUser = this.getCurrentUser();
    return currentUser ? currentUser.email : '';
  }

  /**
   * Test manuel de l'endpoint protégé
   */
  testEndpoint(): void {
    console.log('🧪 Test manuel: Appel de l\'endpoint protégé...');
    
    // Vérifier si l'utilisateur est connecté
    if (!this.isLoggedIn()) {
      console.log('❌ Utilisateur non connecté');
      alert('❌ Vous devez être connecté pour tester l\'endpoint protégé');
      return;
    }
    
    // Afficher les informations de debug
    const currentUser = this.getCurrentUser();
    const accessToken = this.authService.getAccessToken();
    const refreshToken = this.authService.getRefreshToken();
    
    console.log('🔍 Informations de debug:');
    console.log('- Utilisateur connecté:', currentUser);
    console.log('- Access token présent:', !!accessToken);
    console.log('- Refresh token présent:', !!refreshToken);
    console.log('- Access token (premiers caractères):', accessToken ? accessToken.substring(0, 20) + '...' : 'null');
    
    this.authService.testProtectedEndpoint().subscribe({
      next: (response: any) => {
        console.log('✅ Test manuel: Endpoint protégé accessible', response);
        alert('✅ Endpoint protégé accessible ! Voir la console pour les détails.');
      },
      error: (error: any) => {
        console.log('❌ Test manuel: Erreur sur endpoint protégé', error);
        console.log('❌ Status:', error.status);
        console.log('❌ Message:', error.message);
        alert('❌ Erreur sur endpoint protégé ! Voir la console pour les détails.');
      }
    });
  }
}
