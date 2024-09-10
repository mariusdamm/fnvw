import {Component, OnDestroy, OnInit} from '@angular/core';
import {MonthDto} from "../../dtos/month-dto";
import {NgForOf, NgIf} from "@angular/common";
import {AxiosService} from "../../services/axios.service";
import {AuthService} from "../../services/auth.service";
import {UtilService} from "../../services/util.service";
import {PlusButtonComponent} from "../../plus-button/plus-button.component";
import {Subscription} from "rxjs";
import {MonthProviderService} from "../../services/month-provider.service";
import {
  EntrygroupCreateModalComponent
} from "./entrygroup-create-modal/entrygroup-create-modal.component";

@Component({
  selector: 'app-entry-screen',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    PlusButtonComponent,
    EntrygroupCreateModalComponent
  ],
  templateUrl: './entry-screen.component.html',
  styleUrl: './entry-screen.component.css'
})
export class EntryScreenComponent implements OnInit, OnDestroy {

  currentYear: number = new Date().getFullYear();
  currentMonth: number = new Date().getMonth() + 1;
  wholeMonthString = this.currentYear.toString() +
    (this.currentMonth > 9 ? '' : '0') + this.currentMonth.toString();
  months: MonthDto[] | undefined;

  private monthSubscription?: Subscription;

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    protected utilService: UtilService,
    private monthProviderService: MonthProviderService,
  ) {
  }

  ngOnInit() {
    this.monthSubscription = this.monthProviderService.months
      .subscribe(months => this.months = months);

    if (this.monthProviderService.hasMonth(this.wholeMonthString)) {
      return;
    }

    this.fetchMonth(
      this.currentYear.toString(),
      (this.currentMonth > 9 ? '' : '0') + this.currentMonth.toString()
    );
  }

  ngOnDestroy() {
    if (this.monthSubscription)
      this.monthSubscription.unsubscribe();
  }

  fetchMonth(year: string, month: string) {
    this.axiosService.request(
      "GET",
      "/entrygroup/" + year + month,
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
        this.monthProviderService.addMonth(new MonthDto(year+month));
      }
    });
  }

  protected readonly parseInt = parseInt;
  protected readonly parseFloat = parseFloat;
}
