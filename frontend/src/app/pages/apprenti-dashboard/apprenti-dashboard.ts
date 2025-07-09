import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CvUploadComponent } from '../../components/cv-upload/cv-upload';

@Component({
  selector: 'app-apprenti-dashboard',
  templateUrl: './apprenti-dashboard.html',
  standalone: true,
  imports: [CommonModule, CvUploadComponent],
  styleUrls: ['./apprenti-dashboard.css']
})
export class ApprentiDashboardComponent {
  activeTab = 'cv';

  // Méthode pour changer d'onglet
  setActiveTab(tab: string) {
    this.activeTab = tab;
  }
}
