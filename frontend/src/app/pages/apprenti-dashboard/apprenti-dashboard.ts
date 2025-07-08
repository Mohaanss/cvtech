import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-apprenti-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './apprenti-dashboard.html',
})
export class ApprentiDashboardComponent {
  /** état réactif → nom du fichier choisi */
  fileName = signal<string | null>(null);
  /** message d'upload */
  message  = signal<string | null>(null);
  // /** projet : taille max 5 Mo */
  // readonly MAX_SIZE = 5 * 1024 * 1024;
  // /** types acceptés */
  // readonly MIME_OK = ['application/pdf',
  //                     'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
  //                     'application/msword'];

  // constructor(private http: HttpClient) {}

  onFileChange(ev: Event) {
    // const file = (ev.target as HTMLInputElement).files?.[0];
    // if (!file) return;

    // /* validations */
    // if (!this.MIME_OK.includes(file.type)) {
    //   this.message.set('⛔ Format accepté : PDF ou DOCX');
    //   this.fileName.set(null);
    //   return;
    // }
    // if (file.size > this.MAX_SIZE) {
    //   this.message.set('⛔ Taille max : 5 Mo');
    //   this.fileName.set(null);
    //   return;
    // }

    // this.fileName.set(file.name);
    // this.message.set(null);
  }

  upload() {
    // const input = document.getElementById('cvInput') as HTMLInputElement;
    // const file = input.files?.[0];
    // if (!file) return;

    // const form = new FormData();
    // form.append('cv', file);

    // this.http.post('/api/apprenti/upload-cv', form).subscribe({
    //   next: () => this.message.set('✅ CV envoyé avec succès !'),
    //   error: () => this.message.set('⛔ Erreur serveur, réessaie.'),
    // });
  }
}
