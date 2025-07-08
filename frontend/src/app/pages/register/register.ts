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

  constructor(private fb: FormBuilder, private router: Router) {
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
    if (this.form.invalid) return;

    const payload = {
      email: this.form.value.email,
      password: this.form.value.password,
      role: this.role(),
      profile: this.activeGroup.value,
    };

    console.log('Payload envoyé :', payload);
    // TODO : requête HTTP vers l’API
    alert('Inscription réussie ✅');
    this.router.navigateByUrl('/login');
  }
}
