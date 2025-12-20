import { Component, ChangeDetectionStrategy, inject } from '@angular/core';

import { RouterModule } from '@angular/router';
import { HardcodedAuthenticationService } from '../service/hardcoded-authentication.service';
import { BasicAuthenticationService } from '../service/basic-authentication.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterModule]
})
export class MenuComponent {
  readonly hardcodedAuthenticationService = inject(HardcodedAuthenticationService);
  readonly basicAuthenticationService = inject(BasicAuthenticationService);
}
