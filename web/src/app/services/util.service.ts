import {Injectable} from '@angular/core';
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

  calcSumOfGroup(group: EntrygroupDto) {
    let sum = 0;
    group.entries.forEach(entry => sum += entry.value);
    return sum;
  }

  calcSumOfAllGroups(groups: EntrygroupDto[]) {
    let sum = 0;
    groups.forEach(group => sum += this.calcSumOfGroup(group));
    return sum;
  }
}
