import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { ErrorComponent } from './error/error.component';
import { ListPostsComponent } from './list-posts/list-posts.component';
import { LogoutComponent } from './logout/logout.component';
import { RouteGaurdService } from './service/route-gaurd.service';
import { PostComponent } from './post/post.component';
import { CreatePostComponent } from './create-post/create-post.component';

// welcome
const routes: Routes = [
  { path: '', component: LoginComponent }, // canActivate // routeGaurdService
  { path: 'welcome/:name', component: WelcomeComponent, canActivate: [RouteGaurdService] },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [RouteGaurdService] },
  { path: 'posts', component: ListPostsComponent, canActivate: [RouteGaurdService] },
  { path: 'posts/:title', component: PostComponent, canActivate: [RouteGaurdService]},
  { path: 'createpost', component: CreatePostComponent, canActivate: [RouteGaurdService]},
  { path: '**', component: ErrorComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
