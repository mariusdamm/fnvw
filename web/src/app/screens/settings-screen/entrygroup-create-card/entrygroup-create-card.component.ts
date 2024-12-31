import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NgForOf} from "@angular/common";
import {EntrygroupCreateModalComponent} from "../entrygroup-create-modal/entrygroup-create-modal.component";
import {EntrygroupDto} from "../../../dtos/entrygroup-dto";
import {AxiosService} from "../../../services/axios.service";
import {AuthService} from "../../../services/auth.service";
import {toggleIconRotate} from "../../../util";

@Component({
  selector: 'app-entrygroup-create-card',
  standalone: true,
  imports: [
    NgForOf,
    EntrygroupCreateModalComponent
  ],
  templateUrl: './entrygroup-create-card.component.html',
  styleUrl: './entrygroup-create-card.component.css'
})
export class EntrygroupCreateCardComponent implements OnInit{
  groups: EntrygroupDto[] = []
  @ViewChild('collapseIcon') collapseIcon!: ElementRef;

  ngOnInit() {
    this.fetchGroups();
  }

  constructor(
    private readonly axiosService: AxiosService,
    private readonly authService: AuthService,
  ) {
  }

  fetchGroups() {
    this.axiosService.request(
      "GET",
      "/entrygroup",
      ""
    ).then(response => {
      return response.data;
    }).then(groups => {
      if (groups !== null)
        this.groups = groups;
    }).catch(error => {
      if (error.response.status === 401)
        this.authService.deleteJwtToken();
    });
  }

  protected readonly toggleIconRotate = toggleIconRotate;
}
