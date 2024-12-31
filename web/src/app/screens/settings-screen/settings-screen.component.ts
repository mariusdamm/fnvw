import { Component } from '@angular/core';
import {EntrygroupCreateCardComponent} from "./entrygroup-create-card/entrygroup-create-card.component";

@Component({
  selector: 'app-settings-screen',
  standalone: true,
  imports: [
    EntrygroupCreateCardComponent
  ],
  templateUrl: './settings-screen.component.html',
  styleUrl: './settings-screen.component.css'
})
export class SettingsScreenComponent {

}
