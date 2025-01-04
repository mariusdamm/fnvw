import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {EntrygroupDto} from "../dtos/entrygroup-dto";

@Injectable({
  providedIn: 'root'
})
export class GroupProviderService {

  private readonly _groups = new BehaviorSubject<EntrygroupDto[]>([]);

  constructor() { }

  get groups(): Observable<EntrygroupDto[]> {
    return this._groups.asObservable();
  }

  addGroup(group: EntrygroupDto) {
    this._groups.next([...this._groups.getValue(), group]);
  }

  updateMonth(group: EntrygroupDto): boolean {
    const index = this._groups.getValue().findIndex(g => g.id === group.id);
    if (index === -1)
      return false;

    let groups = this._groups.getValue();
    groups.splice(index, 1, group);
    this._groups.next([...groups]);
    return true;
  }
}
