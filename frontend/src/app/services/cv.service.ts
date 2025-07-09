import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { CvUploadDto, CvResponseDto } from '../models/cv.models';

@Injectable({
  providedIn: 'root'
})
export class CvService {
  private readonly API_URL = 'http://localhost:8081/api/cv';

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
   * Upload d'un CV
   */
  uploadCv(cvUploadDto: CvUploadDto): Observable<CvResponseDto> {
    return this.http.post<CvResponseDto>(`${this.API_URL}/upload`, cvUploadDto);
  }

  /**
   * Récupérer les informations du CV
   */
  getCvInfo(): Observable<CvResponseDto> {
    return this.http.get<CvResponseDto>(`${this.API_URL}/info`);
  }

  /**
   * Télécharger le CV
   */
  downloadCv(): Observable<Blob> {
    return this.http.get(`${this.API_URL}/download`, { responseType: 'blob' });
  }

  /**
   * Supprimer le CV
   */
  deleteCv(): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/delete`);
  }

  /**
   * Convertir un fichier en base64
   */
  fileToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      if (!this.isBrowser()) {
        reject(new Error('Cette fonction n\'est disponible que côté client'));
        return;
      }

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        const result = reader.result as string;
        // Enlever le préfixe "data:application/pdf;base64,"
        const base64 = result.split(',')[1];
        resolve(base64);
      };
      reader.onerror = error => reject(error);
    });
  }

  /**
   * Valider un fichier PDF
   */
  validatePdfFile(file: File): { valid: boolean; message?: string } {
    // Vérifier le type MIME
    if (file.type !== 'application/pdf') {
      return { valid: false, message: 'Seuls les fichiers PDF sont acceptés' };
    }

    // Vérifier la taille (max 5MB)
    const maxSize = 5 * 1024 * 1024; // 5MB
    if (file.size > maxSize) {
      return { valid: false, message: 'Le fichier est trop volumineux (max 5MB)' };
    }

    return { valid: true };
  }

  /**
   * Télécharger le CV dans le navigateur
   */
  downloadCvInBrowser(): void {
    if (!this.isBrowser()) return;

    this.downloadCv().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'cv.pdf';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Erreur lors du téléchargement:', error);
        alert('Erreur lors du téléchargement du CV');
      }
    });
  }
} 