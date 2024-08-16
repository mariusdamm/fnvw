import {Component, ElementRef, ViewChild} from '@angular/core';
import {AxiosService} from "../../services/axios.service";
import {UtilService} from "../../services/util.service";
import {AuthService} from "../../services/auth.service";
import {FormsModule} from "@angular/forms";
import {MonthProviderService} from "../../services/month-provider.service";

declare let bootstrap: any;

@Component({
  selector: 'app-month-add-modal',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './month-add-modal.component.html',
  styleUrl: './month-add-modal.component.css'
})
export class MonthAddModalComponent {
  @ViewChild('monthModalYearSelect') monthModalYearSelect!: ElementRef<HTMLSelectElement>;
  @ViewChild('monthModalMonthRadioGroup') monthModalMonthRadioGroup!: ElementRef<HTMLDivElement>;
  currentYear: number = new Date().getFullYear();
  prevYear: number = this.currentYear - 1;
  prevPrevYear: number = this.currentYear - 2;

  constructor(
    private axiosService: AxiosService,
    private utilService: UtilService,
    private authService: AuthService,
    private monthProviderService: MonthProviderService,
  ) {
  }

  fetchMonth(event: Event) {
    event.preventDefault();

    let checkedMonthInput: HTMLInputElement | null =
      this.monthModalMonthRadioGroup.nativeElement.querySelector('input:checked');
    if (checkedMonthInput === null) {
      const intakeLabels =
        this.monthModalMonthRadioGroup.nativeElement.querySelectorAll('label');
      intakeLabels.forEach(label => this.utilService.highlightInvalidInput(label));
      return;
    }

    const year = this.monthModalYearSelect.nativeElement.value;
    const month = checkedMonthInput.value;

    this.axiosService.request(
      "GET",
      "/entrygroup/" + year.toString() + month.toString(),
      ""
    ).then(response => {
      return response.data;
    }).then(month => {
      if (month === null)
        throw new Error("Month is null");

      this.monthProviderService.addMonth(month);

      const bsCollapse = new bootstrap.Collapse('#fetchMonthSuccessCollapse', {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    }).catch(error => {
      if (error.response.status === 401)
        this.authService.deleteJwtToken();
      else if (error.response.status === 404) {
        const bsCollapse = new bootstrap.Collapse('#fetchMonthWarningCollapse', {});
        bsCollapse.show();
        setTimeout(() => bsCollapse.hide(), 3000);
      } else {
        const bsCollapse = new bootstrap.Collapse('#fetchMonthDangerCollapse', {});
        bsCollapse.show();
        setTimeout(() => bsCollapse.hide(), 3000);
      }
    });
  }
}
