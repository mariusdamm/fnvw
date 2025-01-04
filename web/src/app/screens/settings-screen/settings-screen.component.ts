import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
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
export class SettingsScreenComponent implements OnInit, OnDestroy, OnChanges {
  private groupSubscription?: Subscription;
  private groups: EntrygroupDto[] = [];
  protected intakeGroups: EntrygroupDto[] = [];
  protected spendingGroups: EntrygroupDto[] = [];

  ngOnInit() {
    this.groupSubscription = this.groupProvider.groups
      .subscribe(groups => {
        this.groups = groups;
        this.sortGroups();
      });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['groups']) {
      this.sortGroups();
    }
  }

  ngOnDestroy() {
    if (this.groupSubscription)
      this.groupSubscription.unsubscribe();
  }

  constructor(
    private readonly groupProvider: GroupProviderService,
  ) {
  }

  sortGroups() {
    this.intakeGroups = [];
    this.spendingGroups = [];

    this.groups.forEach(group => {
      if (group.isIntake)
        this.intakeGroups.push(group);
      else
        this.spendingGroups.push(group);
    });
  }
}
