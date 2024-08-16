import {EntryTypeDto} from "./entrytype-dto";

export interface EntrygroupDto{
  id: number;
  name: string;
  month: number;
  isIntake: boolean;
  entryTypes: EntryTypeDto[];
}
