import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { 
  LoginDto, 
  LoginResponseDto, 
  CreateAlternantDto, 
  CreateEcoleDto, 
  CreateRecruteurDto,
  UtilisateurResponseDto,
  RefreshTokenRequestDto,
  RefreshTokenResponseDto 
} from '../models/user.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8081/api/utilisateurs';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  /**
   * Vérifie si on est côté client (navigateur)
   */
  private isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  /**
   * Connexion utilisateur
   */
  login(loginData: LoginDto): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(`${this.API_URL}/login`, loginData);
  }

  /**
   * Création compte alternant
   */
  createAlternant(alternantData: CreateAlternantDto): Observable<UtilisateurResponseDto> {
    return this.http.post<UtilisateurResponseDto>(`${this.API_URL}/alternant`, alternantData);
  }

  /**
   * Création compte école
   */
  createEcole(ecoleData: CreateEcoleDto): Observable<UtilisateurResponseDto> {
    return this.http.post<UtilisateurResponseDto>(`${this.API_URL}/ecole`, ecoleData);
  }

  /**
   * Création compte recruteur
   */
  createRecruteur(recruteurData: CreateRecruteurDto): Observable<UtilisateurResponseDto> {
    return this.http.post<UtilisateurResponseDto>(`${this.API_URL}/recruteur`, recruteurData);
  }

  /**
   * Rafraîchissement du token d'accès
   */
  refreshToken(refreshTokenData: RefreshTokenRequestDto): Observable<RefreshTokenResponseDto> {
    return this.http.post<RefreshTokenResponseDto>(`${this.API_URL}/refresh-token`, refreshTokenData);
  }

  /**
   * Sauvegarde des données utilisateur et tokens dans localStorage
   */
  saveUserData(userData: LoginResponseDto): void {
    if (!this.isBrowser()) return;
    
    if (userData.success && userData.id) {
      localStorage.setItem('currentUser', JSON.stringify({
        id: userData.id,
        email: userData.email,
        role: userData.role,
        dateCreation: userData.dateCreation
      }));
      
      if (userData.accessToken) {
        localStorage.setItem('accessToken', userData.accessToken);
      }
      
      if (userData.refreshToken) {
        localStorage.setItem('refreshToken', userData.refreshToken);
      }
    }
  }

  /**
   * Récupération des données utilisateur depuis localStorage
   */
  getCurrentUser(): any {
    if (!this.isBrowser()) return null;
    
    const userData = localStorage.getItem('currentUser');
    return userData ? JSON.parse(userData) : null;
  }

  /**
   * Vérification si l'utilisateur est connecté
   */
  isLoggedIn(): boolean {
    return this.getCurrentUser() !== null && this.hasValidToken();
  }

  /**
   * Déconnexion
   */
  logout(): void {
    if (!this.isBrowser()) return;
    
    localStorage.removeItem('currentUser');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  /**
   * Récupération du token d'accès
   */
  getAccessToken(): string | null {
    if (!this.isBrowser()) return null;
    return localStorage.getItem('accessToken');
  }

  /**
   * Récupération du refresh token
   */
  getRefreshToken(): string | null {
    if (!this.isBrowser()) return null;
    return localStorage.getItem('refreshToken');
  }

  /**
   * Sauvegarde des nouveaux tokens après refresh
   */
  saveTokens(accessToken: string, refreshToken: string): void {
    if (!this.isBrowser()) return;
    
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  /**
   * Vérifie si le token d'accès existe
   */
  hasValidToken(): boolean {
    if (!this.isBrowser()) return false;
    return this.getAccessToken() !== null;
  }

  /**
   * Appel de l'endpoint protégé pour tester l'authentification
   */
  testProtectedEndpoint(): Observable<UtilisateurResponseDto> {
    return this.http.get<UtilisateurResponseDto>(`${this.API_URL}/me`);
  }
} 