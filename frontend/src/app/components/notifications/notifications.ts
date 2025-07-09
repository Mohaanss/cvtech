import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';

interface Notification {
  id: number;
  type: 'info' | 'success' | 'warning' | 'error';
  title: string;
  message: string;
  date: Date;
  read: boolean;
}

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.html',
  standalone: true,
  imports: [CommonModule],
  styleUrls: ['./notifications.css']
})
export class NotificationsComponent implements OnInit {
  notifications: Notification[] = [];
  loading = false;

  constructor() {}

  ngOnInit() {
    this.loadNotifications();
  }

  loadNotifications() {
    this.loading = true;
    // Simulation de chargement des notifications
    setTimeout(() => {
      this.notifications = [
        {
          id: 1,
          type: 'success',
          title: 'CV mis à jour',
          message: 'Votre CV a été mis à jour avec succès. Il est maintenant visible par les recruteurs.',
          date: new Date(Date.now() - 2 * 60 * 60 * 1000), // 2 heures ago
          read: false
        },
        {
          id: 2,
          type: 'info',
          title: 'Nouveau recruteur intéressé',
          message: 'Un recruteur a consulté votre profil. Consultez votre tableau de bord pour plus de détails.',
          date: new Date(Date.now() - 24 * 60 * 60 * 1000), // 1 jour ago
          read: true
        },
        {
          id: 3,
          type: 'warning',
          title: 'Profil incomplet',
          message: 'Votre profil n\'est pas complet. Ajoutez plus d\'informations pour augmenter vos chances.',
          date: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000), // 3 jours ago
          read: true
        }
      ];
      this.loading = false;
    }, 1000);
  }

  markAsRead(notification: Notification) {
    notification.read = true;
  }

  markAllAsRead() {
    this.notifications.forEach(notification => notification.read = true);
  }

  deleteNotification(id: number) {
    this.notifications = this.notifications.filter(n => n.id !== id);
  }

  getUnreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  getNotificationIcon(type: string): string {
    switch (type) {
      case 'success': return '✅';
      case 'info': return 'ℹ️';
      case 'warning': return '⚠️';
      case 'error': return '❌';
      default: return '📢';
    }
  }

  getNotificationColor(type: string): string {
    switch (type) {
      case 'success': return 'border-green-200 bg-green-50';
      case 'info': return 'border-blue-200 bg-blue-50';
      case 'warning': return 'border-yellow-200 bg-yellow-50';
      case 'error': return 'border-red-200 bg-red-50';
      default: return 'border-gray-200 bg-gray-50';
    }
  }

  formatDate(date: Date): string {
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    const minutes = Math.floor(diff / (1000 * 60));
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));

    if (minutes < 60) {
      return `Il y a ${minutes} minute${minutes > 1 ? 's' : ''}`;
    } else if (hours < 24) {
      return `Il y a ${hours} heure${hours > 1 ? 's' : ''}`;
    } else {
      return `Il y a ${days} jour${days > 1 ? 's' : ''}`;
    }
  }
} 