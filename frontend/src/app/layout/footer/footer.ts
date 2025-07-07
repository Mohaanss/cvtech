import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.html',
  standalone: true,
  styleUrls: [],
})
export class FooterComponent {
  year = new Date().getFullYear();
}
