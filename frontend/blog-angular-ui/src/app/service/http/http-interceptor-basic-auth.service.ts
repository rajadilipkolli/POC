import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BasicAuthenticationService } from '../basic-authentication.service';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorBasicAuthService implements HttpInterceptor {

  constructor(
    private basicAuthenticationService: BasicAuthenticationService
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // const username = 'admin';
    // const password = 'admin';
    // const basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);

    const basicAuthHeaderString = this.basicAuthenticationService.getAuthenticatedToken();
    const user = this.basicAuthenticationService.getAuthenticatedUser();

    if (user && basicAuthHeaderString) {
      req = req.clone({
        setHeaders: {
          Authorization: basicAuthHeaderString
        }
      });
    }

    return next.handle(req);
  }
}
