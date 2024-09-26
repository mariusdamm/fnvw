export class EntryCreateDto {
  name: string;
  value: number;
  entryGroupId: number;

  constructor(name: string, value: number, entryGroupId: number) {
    this.name = name;
    this.value = value;
    this.entryGroupId = entryGroupId;
  }
}
