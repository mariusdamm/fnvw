import {Component, OnInit} from '@angular/core';
import {EntrygroupCreateCardComponent} from "./entrygroup-create-card/entrygroup-create-card.component";
import {AxiosService} from "../../services/axios.service";
import {AuthService} from "../../services/auth.service";
import {EntrygroupDto} from "../../dtos/entrygroup-dto";

@Component({
  selector: 'app-settings-screen',
  standalone: true,
  imports: [
    EntrygroupCreateCardComponent
  ],
  templateUrl: './settings-screen.component.html',
  styleUrl: './settings-screen.component.css'
})
export class SettingsScreenComponent implements OnInit {
  private groups: EntrygroupDto[] = [];
  protected intakeGroups: EntrygroupDto[] = [];
  protected spendingGroups: EntrygroupDto[] = [];

  ngOnInit() {
    this.fetchGroups()
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
      if (groups == null)
        return
      this.groups = groups;
      this.sortGroups();
    }).catch(error => {
      if (error.response.status === 401)
        this.authService.deleteJwtToken();
    });
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
