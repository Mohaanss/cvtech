import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Offre {
  id: number;
  intitule: string;
  description: string;
  adresse: string;
  ticketsRestau: boolean;
  teletravail: boolean;
  entreprise: string;
  emailEntreprise: string;
}

@Injectable({ providedIn: 'root' })
export class OffreService {
  constructor(private http: HttpClient) {}

  getOffres(): Observable<Offre[]> {
    return this.http.get<Offre[]>('/api/offres');
  }

  createOffre(offre: Partial<Offre>): Observable<Offre> {
    return this.http.post<Offre>('/api/offres', offre);
  }
} 