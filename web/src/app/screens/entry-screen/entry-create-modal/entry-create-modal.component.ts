import {Component, ElementRef, Input, ViewChild} from '@angular/core';
import {AxiosService} from "../../../services/axios.service";
import {FormsModule} from "@angular/forms";
import {EntryCreateDto} from "../../../dtos/entry-create-dto";
import {AuthService} from "../../../services/auth.service";
import {MonthProviderService} from "../../../services/month-provider.service";
import {UtilService} from "../../../services/util.service";
import {EntrygroupDto} from "../../../dtos/entrygroup-dto";

declare let bootstrap: any;

@Component({
  selector: 'app-entry-create-modal',
  standalone: true,
  imports: [
    FormsModule,
  ],
  templateUrl: './entry-create-modal.component.html',
  styleUrl: './entry-create-modal.component.css'
})
export class EntryCreateModalComponent {

  @ViewChild('entryValueInput') entryModalValueInput!: ElementRef<HTMLInputElement>;
  @ViewChild('entryNameInput') entryModalNameInput!: ElementRef<HTMLInputElement>;

  @Input() entryGroup?: EntrygroupDto;

  constructor(
    private readonly axiosService: AxiosService,
    private readonly authService: AuthService,
    private readonly monthProviderService: MonthProviderService,
    private readonly utilService: UtilService,
  ) {
  }

  postEntry(event: Event) {
    event.preventDefault();

    if (!this.entryGroup?.id)
      return;

    const entryValue = 100 * parseFloat(this.entryModalValueInput.nativeElement.value
      .replace(',', '.'));
    const entryName = this.entryModalNameInput.nativeElement.value;

    if (entryValue === 0) {
      this.utilService.highlightInvalidInput(this.entryModalValueInput.nativeElement);
      return;
    }

    const entryDto = new EntryCreateDto(entryName, entryValue, this.entryGroup.id, new Date());

    this.axiosService.request(
      "POST",
      "/entry",
      entryDto
    ).then(response => {
      return response.data;
    }).then(entry => {
      if (entry === null)
        throw new Error('Entry is null. An Error happened');

      if (!this.entryGroup?.id)
        throw new Error('EntryGroupId passed by parent is undefined');

      this.monthProviderService.addEntryToGroup(entry, this.entryGroup.id);
      const bsCollapse = new bootstrap.Collapse('#postEntrySuccessCollapse_' + this.entryGroup?.id.toString(), {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    }).catch(error => {
      if (error.response.status === 401)
        this.authService.deleteJwtToken();

      const bsCollapse = new bootstrap.Collapse('#postEntryDangerCollapse_' + this.entryGroup?.id.toString(), {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    });
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
}
