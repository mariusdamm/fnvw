import {Injectable} from '@angular/core';
import {EntryTypeDto} from "../dtos/entrytype-dto";
import {EntrygroupDto} from "../dtos/entrygroup-dto";

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor() {
  }

  highlightInvalidInput(element: HTMLElement) {
    element.classList.add('invalid-input-shake', 'border', 'border-danger');
    setTimeout(() => {
      element.classList.remove('invalid-input-shake', 'border', 'border-danger');
    }, 500);
  }

  calcSumOfType(type: EntryTypeDto) {
    let sum = 0;
    type.entries.forEach(entry => sum += entry.value);
    return sum;
  }

  calcSumOfGroup(group: EntrygroupDto) {
    let sum = 0;
    group.entryTypes.forEach(type => sum += this.calcSumOfType(type));
    return sum;
  }

  calcSumOfAllGroups(groups: EntrygroupDto[]) {
    let sum = 0;
    groups.forEach(group => sum += this.calcSumOfGroup(group));
    return sum;
  }
}
