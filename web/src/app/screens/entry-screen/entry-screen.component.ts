import {Component, OnDestroy, OnInit} from '@angular/core';
import {MonthDto} from "../../dtos/month-dto";
import {NgForOf} from "@angular/common";
import {AxiosService} from "../../services/axios.service";
import {AuthService} from "../../services/auth.service";
import {UtilService} from "../../services/util.service";
import {Subscription} from "rxjs";
import {MonthProviderService} from "../../services/month-provider.service";
import {MonthAddModalComponent} from "./month-add-modal/month-add-modal.component";
import {EntrygroupCardComponent} from "./entrygroup-card/entrygroup-card.component";
import {EntrygroupAddModalComponent} from "./entrygroup-add-modal/entrygroup-add-modal.component";

@Component({
  selector: 'app-entry-screen',
  standalone: true,
  imports: [
    NgForOf,
    MonthAddModalComponent,
    EntrygroupCardComponent,
    EntrygroupAddModalComponent
  ],
  templateUrl: './entry-screen.component.html',
  styleUrl: './entry-screen.component.css'
})
export class EntryScreenComponent implements OnInit, OnDestroy {

  months: MonthDto[] | undefined;
  private monthSubscription?: Subscription;

  constructor(
    private readonly axiosService: AxiosService,
    private readonly authService: AuthService,
    protected utilService: UtilService,
    private readonly monthProviderService: MonthProviderService,
  ) {
  }

  ngOnInit() {
    this.monthSubscription = this.monthProviderService.months
      .subscribe(months => this.months = months);

    if (this.monthProviderService.hasMonth(new Date())) {
      return;
    }

    this.fetchMonth(new Date());
  }

  ngOnDestroy() {
    if (this.monthSubscription)
      this.monthSubscription.unsubscribe();
  }

  fetchMonth(date: Date) {
    const formattedDate = date.toISOString().split('.')[0]; // Remove milliseconds and timezone
    this.axiosService.request(
      "GET",
      "/entrygroup/" + formattedDate,
      ""
    ).then(response => {
      return response.data;
    }).then(month => {
      if (month !== null)
        this.monthProviderService.addMonth(month);
    }).catch(error => {
      if (error.response.status === 401)
        this.authService.deleteJwtToken();
      else if (error.response.status === 404) {
        this.monthProviderService.addMonth(new MonthDto(date));
      }
    });
  }

  getDiffBadgeClass(month: MonthDto): string {
    return parseInt(
      (this.utilService.calcSumOfAllGroups(month.intakeGroups) / 100).toFixed(2)
    ) - parseInt(
      (this.utilService.calcSumOfAllGroups(month.spendingGroups) / 100).toFixed(2)
    ) < 0.00 ? 'text-bg-danger' : 'text-bg-success';
  }

  protected readonly parseFloat = parseFloat;
}
