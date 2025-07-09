import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AlternantProfile {
  id: number;
  nom: string;
  prenom: string;
  telephone: string;
  ville: string;
  lienLinkedin: string;
  cvNomFichier?: string;
  cvDateUpload?: string;
  hasCv: boolean;
}

export interface CvInfo {
  cvBase64?: string;
  cvNomFichier?: string;
  cvDateUpload?: string;
  hasCv: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AlternantService {
  private apiUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  // Récupérer le profil de l'alternant
  getProfile(): Observable<AlternantProfile> {
    return this.http.get<AlternantProfile>(`${this.apiUrl}/alternant/profile`);
  }

  // Mettre à jour le profil
  updateProfile(profile: Partial<AlternantProfile>): Observable<AlternantProfile> {
    return this.http.put<AlternantProfile>(`${this.apiUrl}/alternant/profile`, profile);
  }

  // Récupérer les informations du CV
  getCvInfo(): Observable<CvInfo> {
    return this.http.get<CvInfo>(`${this.apiUrl}/cv/info`);
  }

  // Uploader un CV
  uploadCv(cvData: { cvBase64: string; nomFichier: string; typeFichier: string }): Observable<CvInfo> {
    return this.http.post<CvInfo>(`${this.apiUrl}/cv/upload`, cvData);
  }

  // Télécharger le CV
  downloadCv(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/cv/download`, { responseType: 'blob' });
  }

  // Supprimer le CV
  deleteCv(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cv/delete`);
  }
} 