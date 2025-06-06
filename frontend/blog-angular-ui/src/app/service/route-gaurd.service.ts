import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { HardcodedAuthenticationService } from './hardcoded-authentication.service';
import { BasicAuthenticationService } from './basic-authentication.service';

@Injectable({
  providedIn: 'root'
})
export class RouteGaurdService implements CanActivate {

  constructor(
    private hardcodedAuthenticationService: HardcodedAuthenticationService,
    private basicAuthenticationService: BasicAuthenticationService,
    private router: Router
  ) { }

  canActivate(/* route: ActivatedRouteSnapshot, state: RouterStateSnapshot */): boolean {
    // if (this.hardcodedAuthenticationService.isUserLoggedIn()) {
    //   return true;
    // }
    if (this.basicAuthenticationService.isUserLoggedIn()) {
      return true;
    }
    this.router.navigate(['login']);

    return false;
  }
}
