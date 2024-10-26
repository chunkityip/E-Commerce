import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {ApiService} from "../service/api.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-address',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './address.component.html',
  styleUrl: './address.component.css'
})
export class AddressComponent implements OnInit{

  addressForm: FormGroup;
  error: any = null
  isEditMode: boolean;

  constructor(private apiService:ApiService, private fb:FormBuilder, private router:Router) {
    this.isEditMode = this.router.url.includes('edit-address')
    this.addressForm = this.fb.group({})
  }

  ngOnInit() {
    this.addressForm = this.fb.group({
        street: ['', Validators.required],
        city: ['', Validators.required],
        state: ['', Validators.required],
        zipCode: ['', Validators.pattern('^[0-9]*$')],
        country: ['', Validators.required]
      })
  }

}
