import {Component, ElementRef, ViewChild} from '@angular/core';
import {UtilService} from "../../services/util.service";
import {AxiosService} from "../../services/axios.service";
import {AuthService} from "../../services/auth.service";
import {EntrygroupCreateDto} from "../../dtos/entrygroup-create-dto";
import {MonthProviderService} from "../../services/month-provider.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

declare let bootstrap: any;

@Component({
  selector: 'app-entrygroup-create-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './entrygroup-create-modal.component.html',
  styleUrl: './entrygroup-create-modal.component.css'
})
export class EntrygroupCreateModalComponent {

  @ViewChild('groupNameInput') entrygroupModalNameInput!: ElementRef<HTMLInputElement>;
  @ViewChild('groupIntakeButton') entrygroupModalIntakeButton!: ElementRef<HTMLInputElement>;
  @ViewChild('groupSpendingButton') entrygroupModalSpendingButton!: ElementRef<HTMLInputElement>;
  @ViewChild('groupIntakeLabel') entrygroupModalIntakeLabel!: ElementRef<HTMLLabelElement>;
  @ViewChild('groupSpendingLabel') entrygroupModalSpendingLabel!: ElementRef<HTMLLabelElement>;
  currentYear: number = new Date().getFullYear();
  currentMonth: number = new Date().getMonth() + 1;

  constructor(
    private utilService: UtilService,
    private axiosService: AxiosService,
    private authService: AuthService,
    private monthProviderService: MonthProviderService,
  ) {
  }

  postEntrygroup(event: Event) {
    event.preventDefault();

    const groupName = this.entrygroupModalNameInput.nativeElement.value;
    let intake: boolean | undefined = undefined;
    const groupMonth = this.currentYear.toString() +
      (this.currentMonth > 9 ? '' : '0') + this.currentMonth.toString();

    if (this.entrygroupModalIntakeButton.nativeElement.checked)
      intake = true;
    else if (this.entrygroupModalSpendingButton.nativeElement.checked)
      intake = false;

    if (groupName.trim() === '') {
      this.utilService.highlightInvalidInput(this.entrygroupModalNameInput.nativeElement);
    }

    if (intake === undefined) {
      this.utilService.highlightInvalidInput(this.entrygroupModalIntakeLabel.nativeElement);
      this.utilService.highlightInvalidInput(this.entrygroupModalSpendingLabel.nativeElement);
    }

    if (groupName.trim() === '' || intake === undefined)
      return;

    const group = new EntrygroupCreateDto(groupName, Number(groupMonth), intake);

    this.axiosService.request(
      "POST",
      "/entrygroup",
      group,
    ).then(response => {
      return response.data;
    }).then(group => {
      if (group === null)
        throw new Error('Group is null. An Error happened');

      this.monthProviderService.addGroup(group);

      const bsCollapse = new bootstrap.Collapse('#postGroupSuccessCollapse', {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    }).catch(error => {
      if (error.response.status === 401)
        this.authService.deleteJwtToken();

      const bsCollapse = new bootstrap.Collapse('#postGroupDangerCollapse', {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    });
  }
}