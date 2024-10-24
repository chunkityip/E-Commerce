import { Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { CartComponent } from './cart/cart.component';
import { ProfileComponent } from './profile/profile.component';
import { ProductdetailsComponent } from './productdetails/productdetails.component';
import { CategoryComponent } from './category/category.component';


export const routes: Routes = [
  /**PUBLIC ROUTES */
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'product/:productId', component: ProductdetailsComponent},
  {path: 'categories', component: CategoryComponent},
  {path: 'cart', component: CartComponent},

  /**USERS ROUTES */
  {path: 'profile', component: ProfileComponent},

  /**WIDE CARD */
  {path: '', redirectTo: '/home', pathMatch: 'full' },
  {path: '**', redirectTo: '/home'},
];
