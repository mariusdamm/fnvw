import {Component, ElementRef, ViewChild} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {UtilService} from "../../services/util.service";
import {AxiosService} from "../../services/axios.service";
import {AuthService} from "../../services/auth.service";
import {EntrytypeCreateDto} from "../../dtos/entrytype-create-dto";
import {EntrygroupDto} from "../../dtos/entrygroup-dto";
import {MonthProviderService} from "../../services/month-provider.service";
import {NgForOf} from "@angular/common";

declare let bootstrap: any;

@Component({
  selector: 'app-entrytype-create-modal',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './entrytype-create-modal.component.html',
  styleUrl: './entrytype-create-modal.component.css'
})
export class EntrytypeCreateModalComponent {

  @ViewChild('typeIntakeButton') entrytypeModalIntakeButton!: ElementRef<HTMLInputElement>;
  @ViewChild('typeIntakeLabel') entrytypeModalIntakeLabel!: ElementRef<HTMLLabelElement>;
  @ViewChild('typeSpendingButton') entrytypeModalSpendingButton!: ElementRef<HTMLInputElement>;
  @ViewChild('typeSpendingLabel') entrytypeModalSpendingLabel!: ElementRef<HTMLLabelElement>;
  @ViewChild('typeEntryGroupSelect') entrytypeModalGroupSelect!: ElementRef<HTMLSelectElement>;
  @ViewChild('typeNameInput') entrytypeModalNameInput!: ElementRef<HTMLInputElement>;
  currentYear: number = new Date().getFullYear();
  currentMonth: number = new Date().getMonth() + 1;
  wholeMonthString = this.currentYear.toString() +
    (this.currentMonth > 9 ? '' : '0') + this.currentMonth.toString();
  entryGroups: EntrygroupDto[] = [];

  constructor(
    private utilService: UtilService,
    private axiosService: AxiosService,
    private authService: AuthService,
    private monthProviderService: MonthProviderService,
  ) {
  }

  postEntrytype(event: Event) {
    event.preventDefault();

    const typeName = this.entrytypeModalNameInput.nativeElement.value;
    let intake: boolean | undefined = undefined;
    const entryGroupId = parseInt(this.entrytypeModalGroupSelect.nativeElement.value);

    if (this.entrytypeModalIntakeButton.nativeElement.checked)
      intake = true;
    else if (this.entrytypeModalSpendingButton.nativeElement.checked)
      intake = false;

    if (typeName.trim() === '') {
      this.utilService.highlightInvalidInput(this.entrytypeModalNameInput.nativeElement);
    }

    if (intake === undefined) {
      this.utilService.highlightInvalidInput(this.entrytypeModalIntakeLabel.nativeElement);
      this.utilService.highlightInvalidInput(this.entrytypeModalSpendingLabel.nativeElement);
    }

    if (this.entrytypeModalGroupSelect.nativeElement.value.trim() === '') {
      this.utilService.highlightInvalidInput(this.entrytypeModalGroupSelect.nativeElement);
    }

    if (typeName.trim() === '' || intake === undefined || this.entrytypeModalGroupSelect.nativeElement.value.trim() === '')
      return;

    const entryTypeDto = new EntrytypeCreateDto(typeName, entryGroupId);

    this.axiosService.request(
      "POST",
      "/entrytype",
      entryTypeDto,
    ).then(response => {
      return response.data;
    }).then(type => {
      if (type === null)
        throw new Error('Type is null. An Error happened');

      this.monthProviderService.addTypeToGroup(type, type.entryGroupId);

      const bsCollapse = new bootstrap.Collapse('#postTypeSuccessCollapse', {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    }).catch(error => {
      if (error.response.status === 401)
        this.authService.deleteJwtToken();

      const bsCollapse = new bootstrap.Collapse('#postTypeDangerCollapse', {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    });
  }

  loadEntryGroups() {
    let groups: EntrygroupDto[] | undefined = undefined
    if (this.entrytypeModalIntakeButton.nativeElement.checked) {
      groups = this.monthProviderService.getMonth(this.wholeMonthString)?.intakeGroups;
    } else if (this.entrytypeModalSpendingButton.nativeElement.checked) {
      groups = this.monthProviderService.getMonth(this.wholeMonthString)?.spendingGroups;
    }

    if (groups !== undefined) {
      this.entryGroups = groups;
    }
  }
}
