import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, signal } from '@angular/core';
import { RouterModule } from '@angular/router';

interface CvItem {
  id: string;
  nom: string;
  prenom: string;
  email: string;
  role: 'alternant';
  url: string;          // URL du fichier CV (backend doit la renvoyer)
}

@Component({
  selector: 'app-recruteur-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './recruteur-dashboard.html',
})
export class RecruteurDashboardComponent {
  // état réactif : liste des CV et message
  cvs = signal<CvItem[]>([]);
  message = signal<string | null>(null);

  constructor(private http: HttpClient) {
    this.loadCvs();
  }

  /** Récupère la liste des CV */
  loadCvs() {
    this.http.get<CvItem[]>('/api/recruteur/cv-list').subscribe({
      next: res => this.cvs.set(res),
      error: () => this.message.set('⛔ Impossible de charger les CV'),
    });
  }

  /** Télécharge le fichier */
  download(cv: CvItem) {
    window.open(cv.url, '_blank');
  }
}
