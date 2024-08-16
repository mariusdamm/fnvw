import {Component, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {AxiosService} from "../../services/axios.service";

@Component({
  selector: 'app-name-dropdown',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './name-dropdown.component.html',
  styleUrl: './name-dropdown.component.css'
})
export class NameDropdownComponent implements OnInit {

  name: string = '';

  constructor(
    private axiosService: AxiosService,
    private jwtService: AuthService
  ) {
  }

  ngOnInit() {
    this.fetchUser();
  }

  logoutUser() {
    this.jwtService.deleteJwtToken();
    window.location.href = '';
  }

  fetchUser() {
    this.axiosService.request(
      "GET",
      "/user/self",
      ''
    ).then(response => {
      return response.data;
    }).then(self => {
      if(self === null)
        throw new Error("Self is null");

      this.name = self.name;
    }).catch(error => {
      if (error.response.status === 401)
        this.logoutUser();
    });
  }
}
