import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {MonthDto} from "../dtos/month-dto";
import {EntryDto} from "../dtos/entry-dto";

@Injectable({
  providedIn: 'root'
})
export class MonthProviderService {

  private readonly _months = new BehaviorSubject<MonthDto[]>([]);

  constructor() {
  }

  get months(): Observable<MonthDto[]> {
    return this._months.asObservable();
  }

  addMonth(month: MonthDto) {
    this._months.next([...this._months.getValue(), month]);
  }

  hasMonth(month: Date): boolean {
    return this._months.getValue().find(
      m => m.month.getFullYear() === month.getFullYear() && m.month.getMonth() === month.getMonth()
    ) !== undefined;
  }

  updateMonth(month: MonthDto): boolean {
    const index = this._months.getValue().findIndex(
      m => m.month.getFullYear() === month.month.getFullYear() && m.month.getMonth() === month.month.getMonth()
    );
    if (index === -1)
      return false;

    let months = this._months.getValue();
    months.splice(index, 1, month);
    this._months.next([...months]);
    return true;
  }

  getMonth(month: Date): MonthDto | undefined {
    return this._months.getValue().find(
      m => m.month.getFullYear() === month.getFullYear() && m.month.getMonth() === month.getMonth()
    );
  }

  addEntryToGroup(entry: EntryDto, groupId: number) {
    this._months.getValue().forEach(month => {
      let group = month.intakeGroups.find(g => g.id === groupId);
      if (group !== undefined) {
        group.entries.push(entry);
      }

      group = month.spendingGroups.find(g => g.id === groupId);
      if (group !== undefined) {
        group.entries.push(entry);
      }
    });
  }
}
