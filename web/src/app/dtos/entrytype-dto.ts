import {EntryDto} from "./entry-dto";

export interface EntryTypeDto{
  id: number;
  name: string;
  entries: EntryDto[];
  entryGroupId: number;
}
