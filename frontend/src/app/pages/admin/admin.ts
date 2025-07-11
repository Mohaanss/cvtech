import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AdminWidgetComponent } from '../../components/admin-widget/admin-widget';

@Component({
  selector: 'app-admin',
  standalone: true,
  templateUrl: './admin.html',
  styleUrls: ['./admin.css'],
  imports: [CommonModule, RouterModule, AdminWidgetComponent],
})
export class AdminComponent {
  users = [
    {
      id: 1,
      email: 'admin@test.com',
      role: 'ADMIN',
      date: '2024-06-01',
      avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
    },
    {
      id: 2,
      email: 'lea.dupont@gmail.com',
      role: 'ALTERNANT',
      date: '2024-05-30',
      avatar: 'https://randomuser.me/api/portraits/women/2.jpg',
    },
    {
      id: 3,
      email: 'paul.martin@company.com',
      role: 'RECRUTEUR',
      date: '2024-05-29',
      avatar: 'https://randomuser.me/api/portraits/men/3.jpg',
    },
    {
      id: 4,
      email: 'ecole.paris@ecole.fr',
      role: 'ECOLE',
      date: '2024-05-28',
      avatar: 'https://randomuser.me/api/portraits/men/4.jpg',
    },
    {
      id: 5,
      email: 'julie.moreau@gmail.com',
      role: 'ALTERNANT',
      date: '2024-05-27',
      avatar: 'https://randomuser.me/api/portraits/women/5.jpg',
    },
  ];
  logs = [
    {
      date: '2024-06-01 12:01',
      action: 'Suppression du compte user4@test.com',
    },
    {
      date: '2024-06-01 11:58',
      action: 'Blocage du compte paul.martin@company.com',
    },
    { date: '2024-06-01 11:45', action: 'Réinitialisation des stats globales' },
    {
      date: '2024-06-01 11:30',
      action: 'Envoi d’une notification à tous les utilisateurs',
    },
    { date: '2024-06-01 11:15', action: 'Création d’un nouveau compte admin' },
  ];
}
