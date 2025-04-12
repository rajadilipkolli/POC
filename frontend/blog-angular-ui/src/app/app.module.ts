import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { ErrorComponent } from './error/error.component';
import { ListPostsComponent } from './list-posts/list-posts.component';
import { MenuComponent } from './menu/menu.component';
import { FooterComponent } from './footer/footer.component';
import { LogoutComponent } from './logout/logout.component';
import { HttpInterceptorBasicAuthService } from './service/http/http-interceptor-basic-auth.service';
import { PostComponent } from './post/post.component';
import { CreatePostComponent } from './create-post/create-post.component';
import { DatePipe } from '@angular/common';

@NgModule({ declarations: [
        AppComponent,
        LoginComponent,
        WelcomeComponent,
        ErrorComponent,
        ListPostsComponent,
        MenuComponent,
        FooterComponent,
        LogoutComponent,
        PostComponent,
        CreatePostComponent
    ],
    bootstrap: [AppComponent], imports: [BrowserModule,
        AppRoutingModule,
        FormsModule], providers: [
        { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorBasicAuthService, multi: true },
        DatePipe,
        provideHttpClient(withInterceptorsFromDi())
    ] })
export class AppModule { }
