import {EntrygroupDto} from "./entrygroup-dto";

export class MonthDto {
  month: string = "";
  intakeGroups: EntrygroupDto[] = [];
  spendingGroups: EntrygroupDto[] = [];
}
