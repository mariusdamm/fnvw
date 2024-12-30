import {EntrygroupDto} from "./entrygroup-dto";

export class MonthDto {
  month: Date;
  intakeGroups: EntrygroupDto[] = [];
  spendingGroups: EntrygroupDto[] = [];

  constructor(month: Date) {
    this.month = month;
  }
}
