import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CvService } from '../../services/cv.service';
import { CvUploadDto, CvResponseDto } from '../../models/cv.models';

@Component({
  selector: 'app-cv-upload',
  templateUrl: './cv-upload.html',
  standalone: true,
  imports: [CommonModule],
  styleUrls: ['./cv-upload.css']
})
export class CvUploadComponent implements OnInit {
  selectedFile: File | null = null;
  cvInfo: CvResponseDto | null = null;
  isUploading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private cvService: CvService) {}

  ngOnInit(): void {
    this.loadCvInfo();
  }

  /**
   * Charger les informations du CV existant
   */
  loadCvInfo(): void {
    this.cvService.getCvInfo().subscribe({
      next: (info) => {
        this.cvInfo = info;
        console.log('📄 Informations CV chargées:', info);
      },
      error: (error) => {
        console.error('❌ Erreur lors du chargement des infos CV:', error);
        this.cvInfo = { hasCv: false };
      }
    });
  }

  /**
   * Gérer la sélection d'un fichier
   */
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const validation = this.cvService.validatePdfFile(file);
      if (validation.valid) {
        this.selectedFile = file;
        this.errorMessage = '';
        console.log('✅ Fichier sélectionné:', file.name);
      } else {
        this.errorMessage = validation.message || 'Fichier invalide';
        this.selectedFile = null;
        console.log('❌ Fichier rejeté:', validation.message);
      }
    }
  }

  /**
   * Upload du CV
   */
  uploadCv(): void {
    if (!this.selectedFile) {
      this.errorMessage = 'Veuillez sélectionner un fichier';
      return;
    }

    this.isUploading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.cvService.fileToBase64(this.selectedFile).then(base64 => {
      const cvUploadDto: CvUploadDto = {
        cvBase64: base64,
        nomFichier: this.selectedFile!.name,
        typeFichier: 'application/pdf'
      };

      this.cvService.uploadCv(cvUploadDto).subscribe({
        next: (response) => {
          this.isUploading = false;
          this.successMessage = 'CV uploadé avec succès !';
          this.selectedFile = null;
          this.loadCvInfo(); // Recharger les infos
          console.log('✅ CV uploadé avec succès:', response);
        },
        error: (error) => {
          this.isUploading = false;
          this.errorMessage = 'Erreur lors de l\'upload du CV';
          console.error('❌ Erreur upload CV:', error);
        }
      });
    }).catch(error => {
      this.isUploading = false;
      this.errorMessage = 'Erreur lors de la conversion du fichier';
      console.error('❌ Erreur conversion fichier:', error);
    });
  }

  /**
   * Télécharger le CV existant
   */
  downloadCv(): void {
    this.cvService.downloadCvInBrowser();
  }

  /**
   * Supprimer le CV existant
   */
  deleteCv(): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer votre CV ?')) {
      this.cvService.deleteCv().subscribe({
        next: () => {
          this.successMessage = 'CV supprimé avec succès';
          this.loadCvInfo(); // Recharger les infos
          console.log('✅ CV supprimé avec succès');
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la suppression du CV';
          console.error('❌ Erreur suppression CV:', error);
        }
      });
    }
  }

  /**
   * Formater la taille du fichier
   */
  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  /**
   * Formater la date
   */
  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('fr-FR');
  }
} 