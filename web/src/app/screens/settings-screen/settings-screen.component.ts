import {Component, OnDestroy, OnInit} from '@angular/core';
import {EntrygroupCreateCardComponent} from "./entrygroup-create-card/entrygroup-create-card.component";
import {EntrygroupDto} from "../../dtos/entrygroup-dto";
import {GroupProviderService} from "../../services/group-provider.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-settings-screen',
  standalone: true,
  imports: [
    EntrygroupCreateCardComponent
  ],
  templateUrl: './settings-screen.component.html',
  styleUrl: './settings-screen.component.css'
})
export class SettingsScreenComponent implements OnInit, OnDestroy {
  private intakeGroupSubscription?: Subscription;
  private spendingGroupSubscription?: Subscription;
  protected intakeGroups: EntrygroupDto[] = [];
  protected spendingGroups: EntrygroupDto[] = [];

  ngOnInit() {
    this.intakeGroupSubscription = this.groupProvider.intakeGroups
      .subscribe(g => this.intakeGroups = g);
    this.spendingGroupSubscription= this.groupProvider.spendingGroups
      .subscribe(g => this.spendingGroups = g);
  }

  ngOnDestroy() {
    if (this.intakeGroupSubscription)
      this.intakeGroupSubscription.unsubscribe();
    if (this.spendingGroupSubscription)
      this.spendingGroupSubscription.unsubscribe();
  }

  constructor(
    private readonly groupProvider: GroupProviderService,
  ) {
  }
}
