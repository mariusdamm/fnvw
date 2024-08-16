import { Injectable } from '@angular/core';
import axios from 'axios';
import {AuthService} from "./auth.service";


@Injectable({
  providedIn: 'root'
})
export class AxiosService {

  constructor(private jwtService: AuthService) { }

  async request(method: string, url: string, data: any): Promise<any> {
    let headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.jwtService.getJwtToken(),
    };
    return axios({
      method: method,
      url: url,
      data: data,
      headers: headers
    });
  }

  async requestWithoutToken(method: string, url: string, data: any): Promise<any> {
    let headers = {
      'Content-Type': 'application/json',
    };
    return axios({
      method: method,
      url: url,
      data: data,
      headers: headers
    });
  }
}
