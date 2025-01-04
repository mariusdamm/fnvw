import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TemplateProviderService<T> {

  private readonly _value = new BehaviorSubject<T[]>([]);

  constructor() { }

  get value(): Observable<T[]> {
    return this._value.asObservable();
  }

  addValue(value: T) {
    this._value.next([...this._value.getValue(), value]);
  }

  addValues(groups: T[]){
    this._value.next([...this._value.getValue(), ...groups]);
  }
}
