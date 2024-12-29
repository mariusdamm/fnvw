export class EntrygroupCreateDto{
  name: string;
  isIntake: boolean;

  constructor(name: string, isIntake: boolean) {
    this.name = name;
    this.isIntake = isIntake;
  }
}
