export class EntrygroupCreateDto{
  name: string;
  month: number;
  isIntake: boolean;

  constructor(name: string, month: number, isIntake: boolean) {
    this.name = name;
    this.month = month;
    this.isIntake = isIntake;
  }
}
