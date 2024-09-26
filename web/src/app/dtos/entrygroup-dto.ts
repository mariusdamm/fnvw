import {EntryDto} from "./entry-dto";

export interface EntrygroupDto{
  id: number;
  name: string;
  month: number;
  isIntake: boolean;
  entries: EntryDto[];
}
