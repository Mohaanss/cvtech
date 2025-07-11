import { Component, Input, OnInit, OnChanges, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-cv-viewer',
  templateUrl: './cv-viewer.html',
  styleUrls: ['./cv-viewer.css'],
  standalone: true,
  imports: [CommonModule]
})
export class CvViewerComponent implements OnInit, OnChanges {
  @Input() alternantId: number | null = null;
  @Input() showViewer: boolean = false;
  @Output() viewerClosed = new EventEmitter<void>();
  
  safePdfUrl: SafeResourceUrl | null = null;
  isLoading = false;
  error: string | null = null;

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit() {
    if (this.alternantId && this.showViewer) {
      this.loadPdf();
    }
  }

  ngOnChanges() {
    if (this.alternantId && this.showViewer) {
      this.loadPdf();
    } else {
      this.safePdfUrl = null;
    }
  }

  private loadPdf() {
    this.isLoading = true;
    this.error = null;
    
    // Vérifier si on est dans un environnement de navigateur
    if (typeof window === 'undefined' || !window.localStorage) {
      this.error = 'Fonction non disponible dans cet environnement';
      this.isLoading = false;
      return;
    }
    
    // Construire l'URL avec le token d'autorisation
    const token = localStorage.getItem('token');
    if (!token) {
      this.error = 'Non autorisé - veuillez vous connecter';
      this.isLoading = false;
      return;
    }

    const pdfUrl = `/api/cv/view/${this.alternantId}`;
    
    // Pour les iframes avec autorisation, on doit utiliser une approche différente
    // car on ne peut pas passer d'headers dans une iframe
    // On va créer un blob URL à partir d'une requête fetch
    this.fetchPdfAsBlob(pdfUrl, token);
  }

  private async fetchPdfAsBlob(url: string, token: string) {
    try {
      const response = await fetch(url, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (!response.ok) {
        throw new Error('Erreur lors du chargement du CV');
      }

      const blob = await response.blob();
      const blobUrl = URL.createObjectURL(blob);
      this.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(blobUrl);
      this.isLoading = false;
    } catch (error) {
      this.error = 'Impossible de charger le CV';
      this.isLoading = false;
    }
  }

  onClose() {
    this.showViewer = false;
    
    // Nettoyer l'URL blob pour libérer la mémoire
    if (this.safePdfUrl) {
      const url = (this.safePdfUrl as any).changingThisBreaksApplicationSecurity;
      if (url && url.startsWith('blob:')) {
        URL.revokeObjectURL(url);
      }
    }
    
    this.safePdfUrl = null;
    this.viewerClosed.emit();
  }
} 