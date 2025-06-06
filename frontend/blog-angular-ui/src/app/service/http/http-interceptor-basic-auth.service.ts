import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BasicAuthenticationService} from '../basic-authentication.service';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorBasicAuthService implements HttpInterceptor {

  constructor(
    private basicAuthenticationService: BasicAuthenticationService
  ) {
  }

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
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
