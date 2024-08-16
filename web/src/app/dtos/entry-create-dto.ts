export class EntryCreateDto {
  name: string;
  value: number;
  entryTypeId: number;

  constructor(name: string, value: number, entryTypeId: number) {
    this.name = name;
    this.value = value;
    this.entryTypeId = entryTypeId;
  }
}
