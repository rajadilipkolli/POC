import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorBasicAuthService implements HttpInterceptor {

  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const username = 'username';
    const password = 'password';
    const basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);

    req = req.clone({
      setHeaders: {
        Authorization: basicAuthHeaderString
      }
    });

    return next.handle(req);
  }
}
