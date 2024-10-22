import {Component, OnInit} from '@angular/core';
import { CommonModule } from "@angular/common";
import { PaginationComponent} from "../pagination/pagination.component";
import {ProductlistComponent} from "../productlist/productlist.component";
import {ApiService} from "../service/api.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, PaginationComponent, ProductlistComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  constructor(private apiService:ApiService, private router:Router, route:Router) {}
  products: any[] = [];
  currentPage = 1;
  totalPages = 0;
  itemsPerPage = 10;


}
