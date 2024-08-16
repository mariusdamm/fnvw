export class EntrytypeCreateDto {
  name: string;
  entryGroupId: number;

  constructor(name: string, entryGroupId: number) {
    this.name = name;
    this.entryGroupId = entryGroupId;
  }
}
