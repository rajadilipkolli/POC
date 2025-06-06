import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpInterceptorBasicAuthService } from './service/http/http-interceptor-basic-auth.service';
import { DatePipe } from '@angular/common';

@NgModule({
    declarations: [],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        AppComponent
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorBasicAuthService, multi: true },
        DatePipe,
        provideHttpClient(withInterceptorsFromDi())
    ]
})
export class AppModule { }
