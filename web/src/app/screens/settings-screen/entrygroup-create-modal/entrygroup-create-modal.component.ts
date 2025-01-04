import {Component, ElementRef, Input, ViewChild} from '@angular/core';
import {UtilService} from "../../../services/util.service";
import {AxiosService} from "../../../services/axios.service";
import {AuthService} from "../../../services/auth.service";
import {EntrygroupCreateDto} from "../../../dtos/entrygroup-create-dto";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

declare let bootstrap: any;

@Component({
  selector: 'app-entrygroup-create-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
  ],
  templateUrl: './entrygroup-create-modal.component.html',
  styleUrl: './entrygroup-create-modal.component.css'
})
export class EntrygroupCreateModalComponent {
  @Input() isIntake!: boolean;
  @ViewChild('groupNameInput') entrygroupModalNameInput!: ElementRef<HTMLInputElement>;

  constructor(
    private readonly utilService: UtilService,
    private readonly axiosService: AxiosService,
    private readonly authService: AuthService,
  ) {
  }

  postEntrygroup(event: Event) {
    event.preventDefault();

    const groupName = this.entrygroupModalNameInput.nativeElement.value;

    if (groupName.trim() === '') {
      this.utilService.highlightInvalidInput(this.entrygroupModalNameInput.nativeElement);
    }

    if (groupName.trim() === '')
      return;

    const group = new EntrygroupCreateDto(groupName, this.isIntake);

    this.axiosService.request(
      "POST",
      "/entrygroup",
      group,
    ).then(response => {
      return response.data;
    }).then(group => {
      if (group === null)
        throw new Error('Group is null. An Error happened');

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
