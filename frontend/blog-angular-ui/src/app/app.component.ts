import { Component, ChangeDetectionStrategy, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MenuComponent } from './menu/menu.component';
import { FooterComponent } from './footer/footer.component';

@Component({
  selector: 'app-root',
  template: `
    <!-- Menus-->
    <app-menu />

    <div class="container">
      <router-outlet />
    </div>

    <!-- Footer -->
    <app-footer />
  `,
  styleUrls: ['./app.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterOutlet, MenuComponent, FooterComponent]
})
export class AppComponent {
  title = signal('blog-angular-ui');
}
