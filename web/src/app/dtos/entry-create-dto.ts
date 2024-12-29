export class EntryCreateDto {
  name: string;
  value: number;
  entryGroupId: number;
  date: Date;

  constructor(name: string, value: number, entryGroupId: number, date: Date) {
    this.name = name;
    this.value = value;
    this.entryGroupId = entryGroupId;
    this.date = date;
  }
}
