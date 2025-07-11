import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CvUploadComponent } from '../../components/cv-upload/cv-upload';
import { ProfileCardComponent } from '../../components/profile-card/profile-card';
import { CvStatsComponent } from '../../components/cv-stats/cv-stats';
import { NotificationsComponent } from '../../components/notifications/notifications';
import { CvViewerComponent } from '../../components/cv-viewer/cv-viewer';

@Component({
  selector: 'app-apprenti-dashboard',
  templateUrl: './apprenti-dashboard.html',
  standalone: true,
  imports: [CommonModule, CvUploadComponent, ProfileCardComponent, CvStatsComponent, NotificationsComponent, CvViewerComponent],
  styleUrls: ['./apprenti-dashboard.css']
})
export class ApprentiDashboardComponent implements OnInit {
  activeTab = 'overview';

  // Propriétés pour le visualiseur de CV
  showCvViewer: boolean = false;
  currentUserId: number | null = null;

  constructor() {}

  ngOnInit() {
    // Récupérer l'ID de l'utilisateur connecté de manière sécurisée
    if (typeof window !== 'undefined' && window.localStorage) {
      const userId = localStorage.getItem('userId');
      this.currentUserId = userId ? parseInt(userId) : null;
    }
  }

  // Méthode pour changer d'onglet
  setActiveTab(tab: string) {
    this.activeTab = tab;
  }

  // Méthodes pour le visualiseur de CV
  viewMyCv() {
    if (this.currentUserId) {
      this.showCvViewer = true;
    }
  }

  closeCvViewer() {
    this.showCvViewer = false;
  }
}
