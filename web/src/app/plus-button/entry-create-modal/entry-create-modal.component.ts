import {Component, ElementRef, ViewChild} from '@angular/core';
import {AxiosService} from "../../services/axios.service";
import {FormsModule} from "@angular/forms";
import {EntrygroupDto} from "../../dtos/entrygroup-dto";
import {EntryCreateDto} from "../../dtos/entry-create-dto";
import {AuthService} from "../../services/auth.service";
import {MonthProviderService} from "../../services/month-provider.service";
import {UtilService} from "../../services/util.service";
import {NgForOf} from "@angular/common";
import {EntryTypeDto} from "../../dtos/entrytype-dto";

declare let bootstrap: any;

@Component({
  selector: 'app-entry-create-modal',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './entry-create-modal.component.html',
  styleUrl: './entry-create-modal.component.css'
})
export class EntryCreateModalComponent {

  @ViewChild('entryIntakeButton') entryModalIntakeButton!: ElementRef<HTMLInputElement>;
  @ViewChild('entryIntakeLabel') entryModalIntakeLabel!: ElementRef<HTMLLabelElement>;
  @ViewChild('entrySpendingButton') entryModalSpendingButton!: ElementRef<HTMLInputElement>;
  @ViewChild('entrySpendingLabel') entryModalSpendingLabel!: ElementRef<HTMLLabelElement>;
  @ViewChild('entryGroupSelect') entryModalGroupSelect!: ElementRef<HTMLSelectElement>;
  @ViewChild('entryTypeSelect') entryModalTypeSelect!: ElementRef<HTMLSelectElement>;
  @ViewChild('entryValueInput') entryModalValueInput!: ElementRef<HTMLInputElement>;
  @ViewChild('entryNameInput') entryModalNameInput!: ElementRef<HTMLInputElement>;
  currentYear: number = new Date().getFullYear();
  currentMonth: number = new Date().getMonth() + 1;
  wholeMonthString = this.currentYear.toString() +
    (this.currentMonth > 9 ? '' : '0') + this.currentMonth.toString();
  entryGroups: EntrygroupDto[] = [];
  entryTypes: EntryTypeDto[] = [];

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private monthProviderService: MonthProviderService,
    private utilService: UtilService,
  ) {
  }

  postEntry(event: Event) {
    event.preventDefault();

    const entryTypeId = parseInt(this.entryModalTypeSelect.nativeElement.value);
    const entryGroupId = parseInt(this.entryModalGroupSelect.nativeElement.value);
    const entryValue = 100 * parseFloat(this.entryModalValueInput.nativeElement.value
      .replace(',', '.'));
    const entryName = this.entryModalNameInput.nativeElement.value;

    let isIntakeChecked = this.entryModalIntakeButton.nativeElement.checked ||
      this.entryModalSpendingButton.nativeElement.checked;
    if (!isIntakeChecked) {
      this.utilService.highlightInvalidInput(this.entryModalIntakeLabel.nativeElement);
      this.utilService.highlightInvalidInput(this.entryModalSpendingLabel.nativeElement);
    }
    if (this.entryModalGroupSelect.nativeElement.value.trim() === '')
      this.utilService.highlightInvalidInput(this.entryModalGroupSelect.nativeElement);
    if (this.entryModalTypeSelect.nativeElement.value.trim() === '')
      this.utilService.highlightInvalidInput(this.entryModalTypeSelect.nativeElement);
    if (entryValue === 0)
      this.utilService.highlightInvalidInput(this.entryModalValueInput.nativeElement);

    if (entryValue === 0 || this.entryModalTypeSelect.nativeElement.value.trim() === '' || !isIntakeChecked ||
      this.entryModalGroupSelect.nativeElement.value.trim() === '')
      return;

    const entryDto = new EntryCreateDto(entryName, entryValue, entryTypeId);

    this.axiosService.request(
      "POST",
      "/entry",
      entryDto
    ).then(response => {
      return response.data;
    }).then(entry => {
      if (entry === null)
        throw new Error('Type is null. An Error happened');

      this.monthProviderService.addEntryToType(entry, entryGroupId, entryTypeId);
      const bsCollapse = new bootstrap.Collapse('#postEntrySuccessCollapse', {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    }).catch(error => {
      if (error.response.status === 401)
        this.authService.deleteJwtToken();

      const bsCollapse = new bootstrap.Collapse('#postEntryDangerCollapse', {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    });
  }

  loadEntryGroups() {
    let groups: EntrygroupDto[] | undefined = undefined
    if (this.entryModalIntakeButton.nativeElement.checked) {
      groups = this.monthProviderService.getMonth(this.wholeMonthString)?.intakeGroups;
    } else if (this.entryModalSpendingButton.nativeElement.checked) {
      groups = this.monthProviderService.getMonth(this.wholeMonthString)?.spendingGroups;
    }

    if (groups !== undefined) {
      this.entryGroups = groups;
    }
  }

  loadEntryTypes() {
    const selectedGroupId = parseInt(this.entryModalGroupSelect.nativeElement.value);
    let types: EntryTypeDto[] | undefined;
    if (this.entryModalIntakeButton.nativeElement.checked) {
      this.monthProviderService.getMonth(this.wholeMonthString)?.intakeGroups.forEach(group => {
        if (group.id === selectedGroupId) {
          types = group.entryTypes;
        }
      });
    } else if (this.entryModalSpendingButton.nativeElement.checked) {
      this.monthProviderService.getMonth(this.wholeMonthString)?.spendingGroups.forEach(group => {
        if (group.id === selectedGroupId) {
          types = group.entryTypes;
        }
      });
    }

    if (types !== undefined) {
      this.entryTypes = types;
    }
  }

  validateEntryValueInput() {
    let inputField = this.entryModalValueInput.nativeElement;
    let pattern = /^\d+(,\d{2})?€?$/;
    if (inputField.value === '')
      inputField.value = '0€'
    if (!pattern.test(inputField.value))
      this.utilService.highlightInvalidInput(inputField);
    else if (!inputField.value.endsWith('€'))
      inputField.value += '€';
  }

  handleFocusEntryValueInput() {
    let inputField = this.entryModalValueInput.nativeElement;
    if (inputField.value === '0€')
      inputField.value = '';
    inputField.setSelectionRange(0, 0);
  }

  removeLettersEntryValueInput() {
    let inputField = this.entryModalValueInput.nativeElement;
    const pattern = /[^0-9,]/g;
    inputField.value = inputField.value.replace(pattern, '');
  }

  resetSelects() {
    this.entryModalGroupSelect.nativeElement.value = '';
    this.entryModalTypeSelect.nativeElement.value = '';
  }
}
