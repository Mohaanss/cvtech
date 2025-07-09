import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AlternantService, CvInfo } from '../../services/alternant.service';

@Component({
  selector: 'app-cv-stats',
  templateUrl: './cv-stats.html',
  standalone: true,
  imports: [CommonModule],
  styleUrls: ['./cv-stats.css']
})
export class CvStatsComponent implements OnInit {
  cvInfo: CvInfo | null = null;
  loading = false;
  error = '';

  constructor(private alternantService: AlternantService) {}

  ngOnInit() {
    this.loadCvInfo();
  }

  loadCvInfo() {
    this.loading = true;
    this.alternantService.getCvInfo().subscribe({
      next: (cvInfo: CvInfo) => {
        this.cvInfo = cvInfo;
        this.loading = false;
      },
      error: (error: any) => {
        this.error = 'Erreur lors du chargement des informations CV';
        this.loading = false;
        console.error('Erreur CV info:', error);
      }
    });
  }

  downloadCv() {
    this.alternantService.downloadCv().subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = this.cvInfo?.cvNomFichier || 'cv.pdf';
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error: any) => {
        this.error = 'Erreur lors du téléchargement';
        console.error('Erreur download:', error);
      }
    });
  }

  deleteCv() {
    if (confirm('Êtes-vous sûr de vouloir supprimer votre CV ?')) {
      this.alternantService.deleteCv().subscribe({
        next: () => {
          this.loadCvInfo(); // Recharger les informations
        },
        error: (error: any) => {
          this.error = 'Erreur lors de la suppression';
          console.error('Erreur delete:', error);
        }
      });
    }
  }

  getFileSize(): string {
    if (!this.cvInfo?.cvBase64) return '0 KB';
    const sizeInBytes = this.cvInfo.cvBase64.length * 3 / 4;
    const sizeInKB = Math.round(sizeInBytes / 1024);
    return `${sizeInKB} KB`;
  }

  getUploadDate(): string {
    if (!this.cvInfo?.cvDateUpload) return 'Non disponible';
    return new Date(this.cvInfo.cvDateUpload).toLocaleDateString('fr-FR');
  }
} 