import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CvViewerComponent } from '../../components/cv-viewer/cv-viewer';

@Component({
  selector: 'app-alternant-search',
  standalone: true,
  imports: [CommonModule, FormsModule, CvViewerComponent],
  templateUrl: './alternant-search.html',
})
export class AlternantSearchComponent implements OnInit {
  alternants: any[] = [];
  filteredAlternants: any[] = [];
  searchTerm: string = '';
  
  // Propriétés pour le visualiseur de CV
  selectedAlternantId: number | null = null;
  showCvViewer: boolean = false;
  userRole: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Récupérer le rôle de l'utilisateur connecté de manière sécurisée
    if (typeof window !== 'undefined' && window.localStorage) {
      this.userRole = localStorage.getItem('role');
    }
    
    this.http.get<any[]>('http://localhost:8081/api/utilisateurs/public/alternants').subscribe({
      next: (data) => {
        this.alternants = data;
        this.filteredAlternants = data;
      },
      error: () => {
        this.alternants = [];
        this.filteredAlternants = [];
      }
    });
  }

  filterAlternants() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredAlternants = this.alternants;
      return;
    }
    this.filteredAlternants = this.alternants.filter(alt =>
      (alt.nom && alt.nom.toLowerCase().includes(term)) ||
      (alt.prenom && alt.prenom.toLowerCase().includes(term)) ||
      (alt.ecole && alt.ecole.toLowerCase().includes(term)) ||
      (alt.categorieEtude && alt.categorieEtude.toLowerCase().includes(term))
    );
  }

  // Méthodes pour le visualiseur de CV
  viewCv(alternantId: number) {
    if (this.userRole !== 'RECRUTEUR') {
      alert('Seuls les recruteurs peuvent visualiser les CVs');
      return;
    }
    
    this.selectedAlternantId = alternantId;
    this.showCvViewer = true;
  }

  closeCvViewer() {
    this.showCvViewer = false;
    this.selectedAlternantId = null;
  }

  isRecruteur(): boolean {
    return this.userRole === 'RECRUTEUR';
  }
} 