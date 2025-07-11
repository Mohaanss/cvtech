import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OffreService, Offre } from '../../services/offre.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-offre',
  templateUrl: './create-offre.html',
  styleUrls: ['./create-offre.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CreateOffreComponent {
  offre: Partial<Offre> = {
    intitule: '',
    description: '',
    adresse: '',
    ticketsRestau: false,
    teletravail: false
  };
  message: string | null = null;
  error: string | null = null;

  constructor(private offreService: OffreService, private router: Router) {}

  submit() {
    this.offreService.createOffre(this.offre).subscribe({
      next: (res) => {
        this.message = "Offre créée avec succès !";
        setTimeout(() => this.router.navigate(['/offres']), 1000);
      },
      error: (err) => {
        this.error = "Erreur lors de la création de l’offre.";
      }
    });
  }
} 