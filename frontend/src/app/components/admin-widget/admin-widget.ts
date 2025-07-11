import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'admin-widget',
  standalone: true,
  templateUrl: './admin-widget.html',
  styleUrls: ['./admin-widget.css'],
  imports: [CommonModule],
})
export class AdminWidgetComponent {
  @Input() note: string = '';
  @Input() icon: string = '';
}
