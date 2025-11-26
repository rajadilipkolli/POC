import { Injectable, inject } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { HardcodedAuthenticationService } from './hardcoded-authentication.service';
import { BasicAuthenticationService } from './basic-authentication.service';

@Injectable({
  providedIn: 'root'
})
export class RouteGaurdService implements CanActivate {
  private readonly hardcodedAuthenticationService = inject(HardcodedAuthenticationService);
  private readonly basicAuthenticationService = inject(BasicAuthenticationService);
  private readonly router = inject(Router);

  canActivate(): boolean {
    if (this.basicAuthenticationService.isUserLoggedIn()) {
      return true;
    }
    this.router.navigate(['login']);
    return false;
  }
}
