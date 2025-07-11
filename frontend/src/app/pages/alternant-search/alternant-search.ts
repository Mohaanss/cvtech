import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-alternant-search',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './alternant-search.html',
})
export class AlternantSearchComponent implements OnInit {
  alternants: any[] = [];
  filteredAlternants: any[] = [];
  searchTerm: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit() {
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
} 