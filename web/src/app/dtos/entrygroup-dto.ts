import {EntryDto} from "./entry-dto";

export interface EntrygroupDto{
  id: number;
  name: string;
  isIntake: boolean;
  entries: EntryDto[];
}
