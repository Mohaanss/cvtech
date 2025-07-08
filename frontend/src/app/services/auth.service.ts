import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  LoginDto, 
  LoginResponseDto, 
  CreateAlternantDto, 
  CreateEcoleDto, 
  CreateRecruteurDto,
  UtilisateurResponseDto 
} from '../models/user.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8081/api/utilisateurs';

  constructor(private http: HttpClient) {}

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
   * Sauvegarde des données utilisateur dans localStorage
   */
  saveUserData(userData: LoginResponseDto): void {
    if (userData.success && userData.id) {
      localStorage.setItem('currentUser', JSON.stringify({
        id: userData.id,
        email: userData.email,
        role: userData.role,
        dateCreation: userData.dateCreation
      }));
    }
  }

  /**
   * Récupération des données utilisateur depuis localStorage
   */
  getCurrentUser(): any {
    const userData = localStorage.getItem('currentUser');
    return userData ? JSON.parse(userData) : null;
  }

  /**
   * Vérification si l'utilisateur est connecté
   */
  isLoggedIn(): boolean {
    return this.getCurrentUser() !== null;
  }

  /**
   * Déconnexion
   */
  logout(): void {
    localStorage.removeItem('currentUser');
  }
} 