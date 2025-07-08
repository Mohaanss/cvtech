// src/app/pages/register/register.ts
import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { 
  CreateAlternantDto, 
  CreateEcoleDto, 
  CreateRecruteurDto 
} from '../../models/user.models';

type Role = 'alternant' | 'ecole' | 'recruteur';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.html',
})
export class RegisterComponent {
  /** valeurs possibles pour l’UI */
  readonly roles = ['alternant', 'ecole', 'recruteur'] as const;

  /** rôle sélectionné (signal réactif) */
  role = signal<Role>('alternant');

  /** formulaire racine */
  form!: ReturnType<FormBuilder['group']>;

  /** État du chargement */
  isLoading = signal(false);

  /** Messages d'erreur ou de succès */
  message = signal<{ text: string; type: 'error' | 'success' } | null>(null);

  constructor(
    private fb: FormBuilder, 
    private router: Router,
    private authService: AuthService
  ) {
    this.buildForm();
  }

  /** création du formGroup */
  private buildForm(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],

      alternant: this.fb.group({
        nom: [''],
        prenom: [''],
        telephone: [''],
        ville: [''],
        lien_linkedin: [''],
        lien_portfolio: [''],
        date_naissance: [''],
      }),
      ecole: this.fb.group({
        nom: [''],
        adresse: [''],
        site_web: [''],
      }),
      recruteur: this.fb.group({
        entreprise: [''],
        service: [''],
        telephone: [''],
        site_web: [''],
      }),
    });
  }

  /** sous-groupe actif selon le rôle */
  get activeGroup(): AbstractControl {
    return this.form.get(this.role())!;
  }

  /** sélection d’un nouveau rôle */
changeRole(r: string): void {
  this.role.set(r as Role);   // cast unique ici
  this.activeGroup.reset({});
}

  /** envoi du formulaire */
  onSubmit(): void {
    if (this.form.invalid) {
      this.message.set({ text: 'Veuillez remplir tous les champs requis.', type: 'error' });
      return;
    }

    this.isLoading.set(true);
    this.message.set(null);

    const formData = this.form.value;
    const profileData = this.activeGroup.value;

    switch (this.role()) {
      case 'alternant':
        this.createAlternantAccount(formData, profileData);
        break;
      case 'ecole':
        this.createEcoleAccount(formData, profileData);
        break;
      case 'recruteur':
        this.createRecruteurAccount(formData, profileData);
        break;
    }
  }

  private createAlternantAccount(formData: any, profileData: any): void {
    const dto: CreateAlternantDto = {
      email: formData.email,
      motDePasse: formData.password,
      nom: profileData.nom,
      prenom: profileData.prenom,
      telephone: profileData.telephone,
      ville: profileData.ville,
      lienLinkedin: profileData.lien_linkedin,
      lienPortfolio: profileData.lien_portfolio,
      dateNaissance: profileData.date_naissance
    };

    this.authService.createAlternant(dto).subscribe({
      next: () => this.handleSuccess(),
      error: (error) => this.handleError(error)
    });
  }

  private createEcoleAccount(formData: any, profileData: any): void {
    const dto: CreateEcoleDto = {
      email: formData.email,
      motDePasse: formData.password,
      nomEcole: profileData.nom,
      adresse: profileData.adresse,
      siteWeb: profileData.site_web
    };

    this.authService.createEcole(dto).subscribe({
      next: () => this.handleSuccess(),
      error: (error) => this.handleError(error)
    });
  }

  private createRecruteurAccount(formData: any, profileData: any): void {
    const dto: CreateRecruteurDto = {
      email: formData.email,
      motDePasse: formData.password,
      entreprise: profileData.entreprise,
      service: profileData.service,
      telephone: profileData.telephone,
      siteWeb: profileData.site_web
    };

    this.authService.createRecruteur(dto).subscribe({
      next: () => this.handleSuccess(),
      error: (error) => this.handleError(error)
    });
  }

  private handleSuccess(): void {
    this.isLoading.set(false);
    this.message.set({ text: 'Inscription réussie ! Redirection vers la connexion...', type: 'success' });
    
    // Redirection après 2 secondes
    setTimeout(() => {
      this.router.navigateByUrl('/login');
    }, 2000);
  }

  private handleError(error: any): void {
    this.isLoading.set(false);
    console.error('Erreur lors de l\'inscription:', error);
    
    let errorMessage = 'Une erreur est survenue lors de l\'inscription.';
    
    if (error.status === 409) {
      errorMessage = 'Un compte avec cet email existe déjà.';
    } else if (error.status === 400) {
      errorMessage = 'Données invalides. Veuillez vérifier vos informations.';
    }
    
    this.message.set({ text: errorMessage, type: 'error' });
  }
}
