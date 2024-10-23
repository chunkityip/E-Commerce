import { Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { CartComponent } from './cart/cart.component';

export const routes: Routes = [
  /**PUBLIC ROUTES */
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'cart', component: CartComponent},

  /**WIDE CARD */
  {path: '', redirectTo: '/home', pathMatch: 'full' },
  {path: '**', redirectTo: '/home'},
];
