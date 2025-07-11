import { Component, OnInit } from '@angular/core';
import { OffreService, Offre } from '../../services/offre.service';

@Component({
  selector: 'app-offres',
  templateUrl: './offres.html',
  styleUrls: ['./offres.css']
})
export class OffresComponent implements OnInit {
  offres: Offre[] = [];
  selectedEmail: string | null = null;
  isRecruteur = localStorage.getItem('role') === 'RECRUTEUR';

  constructor(private offreService: OffreService) {}

  ngOnInit() {
    this.offreService.getOffres().subscribe(data => this.offres = data);
  }

  showEmail(email: string) {
    this.selectedEmail = email;
  }
} 