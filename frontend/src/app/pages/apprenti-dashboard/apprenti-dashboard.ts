import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CvUploadComponent } from '../../components/cv-upload/cv-upload';
import { ProfileCardComponent } from '../../components/profile-card/profile-card';
import { CvStatsComponent } from '../../components/cv-stats/cv-stats';
import { NotificationsComponent } from '../../components/notifications/notifications';

@Component({
  selector: 'app-apprenti-dashboard',
  templateUrl: './apprenti-dashboard.html',
  standalone: true,
  imports: [CommonModule, CvUploadComponent, ProfileCardComponent, CvStatsComponent, NotificationsComponent],
  styleUrls: ['./apprenti-dashboard.css']
})
export class ApprentiDashboardComponent {
  activeTab = 'overview';

  // Méthode pour changer d'onglet
  setActiveTab(tab: string) {
    this.activeTab = tab;
  }
}
