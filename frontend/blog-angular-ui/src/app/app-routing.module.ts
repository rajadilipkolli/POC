import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { ErrorComponent } from './error/error.component';
import { ListPostsComponent } from './list-posts/list-posts.component';

// welcome
const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'welcome/:name', component: WelcomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'posts', component: ListPostsComponent },
  { path: '**', component: ErrorComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
