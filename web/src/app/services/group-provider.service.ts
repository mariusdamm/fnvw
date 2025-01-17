import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {EntrygroupDto} from "../dtos/entrygroup-dto";

@Injectable({
  providedIn: 'root'
})
export class GroupProviderService {

  private readonly _groups = new BehaviorSubject<EntrygroupDto[]>([]);
  private readonly _intakeGroups = new BehaviorSubject<EntrygroupDto[]>([]);
  private readonly _spendingGroups = new BehaviorSubject<EntrygroupDto[]>([]);

  constructor() { }

  get groups(): Observable<EntrygroupDto[]> {
    return this._groups.asObservable();
  }

  get intakeGroups(): Observable<EntrygroupDto[]> {
    return this._intakeGroups.asObservable();
  }

  get spendingGroups(): Observable<EntrygroupDto[]> {
    return this._spendingGroups.asObservable();
  }

  addGroup(group: EntrygroupDto) {
    this._groups.next([...this._groups.getValue(), group]);
    this.sortGroups();
  }

  addGroups(groups: EntrygroupDto[]){
    this._groups.next([...this._groups.getValue(), ...groups]);
    this.sortGroups();
  }

  updateGroup(group: EntrygroupDto): boolean {
    const index = this._groups.getValue().findIndex(g => g.id === group.id);
    if (index === -1)
      return false;

    let groups = this._groups.getValue();
    groups.splice(index, 1, group);
    this._groups.next([...groups]);
    this.sortGroups()
    return true;
  }

  private sortGroups() {
    let intakeGroups: EntrygroupDto[] = [];
    let spendingGroups: EntrygroupDto[] = [];

    this._groups.getValue().forEach(group => {
      if (group.isIntake)
        intakeGroups.push(group);
      else
        spendingGroups.push(group);
    });

    this._intakeGroups.next([...intakeGroups]);
    this._spendingGroups.next([...spendingGroups]);
  }
}
