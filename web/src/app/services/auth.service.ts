import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private readonly LOCAL_STORAGE_JWT_TOKEN_KEY: string = 'jwtToken';
  private _isLoggedIn = new BehaviorSubject<boolean>(this.hasToken());

  constructor() {
  }

  get isLoggedIn() {
    return this._isLoggedIn.asObservable();
  }

  private hasToken(){
    return !!localStorage.getItem(this.LOCAL_STORAGE_JWT_TOKEN_KEY);
  }

  saveJwtToken(jwtToken: string) {
    localStorage.setItem(this.LOCAL_STORAGE_JWT_TOKEN_KEY, jwtToken);
    this._isLoggedIn.next(true);
  }

  getJwtToken() {
    return localStorage.getItem(this.LOCAL_STORAGE_JWT_TOKEN_KEY);
  }

  deleteJwtToken() {
    localStorage.removeItem(this.LOCAL_STORAGE_JWT_TOKEN_KEY);
    this._isLoggedIn.next(false);
    window.location.href = '';
  }
}
