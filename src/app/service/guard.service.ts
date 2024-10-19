import {CanActivateFn, Router} from "@angular/router";
import {ApiService } from "./api.service";
import {inject, Inject} from "@angular/core";

export const userGuard: CanActivateFn = (route, state) => {
  if (inject(ApiService).isAuthenticated()) {
    return true; // allow accessing that endpoint
  }else{
    inject(Router).navigate(['/login'])
    return false; //not allowing
  }
};

export const adminGuard: CanActivateFn = (route, state) => {
  if (inject(ApiService).isAuthenticated()) {
    return true; // allow accessing that endpoint
  }else{
    inject(Router).navigate(['/login'])
    return false; //not allowing
  }
};


