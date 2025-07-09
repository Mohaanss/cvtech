import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AlternantService, AlternantProfile } from '../../services/alternant.service';

@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.html',
  standalone: true,
  imports: [CommonModule, FormsModule],
  styleUrls: ['./profile-card.css']
})
export class ProfileCardComponent implements OnInit {
  profile: AlternantProfile | null = null;
  isEditing = false;
  loading = false;
  error = '';

  constructor(private alternantService: AlternantService) {}

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.loading = true;
    this.alternantService.getProfile().subscribe({
      next: (profile: AlternantProfile) => {
        this.profile = profile;
        this.loading = false;
      },
      error: (error: any) => {
        this.error = 'Erreur lors du chargement du profil';
        this.loading = false;
        console.error('Erreur profil:', error);
      }
    });
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  saveProfile() {
    if (!this.profile) return;

    this.loading = true;
    this.alternantService.updateProfile(this.profile).subscribe({
      next: (updatedProfile: AlternantProfile) => {
        this.profile = updatedProfile;
        this.isEditing = false;
        this.loading = false;
        this.error = '';
      },
      error: (error: any) => {
        this.error = 'Erreur lors de la sauvegarde';
        this.loading = false;
        console.error('Erreur sauvegarde:', error);
      }
    });
  }

  cancelEdit() {
    this.isEditing = false;
    this.loadProfile(); // Recharger les données originales
  }
} 