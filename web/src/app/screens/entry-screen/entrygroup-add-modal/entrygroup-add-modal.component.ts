import {Component, ElementRef, Input, ViewChild} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {EntrygroupDto} from "../../../dtos/entrygroup-dto";
import {NgForOf} from "@angular/common";
import {GroupProviderService} from "../../../services/group-provider.service";

@Component({
  selector: 'app-entrygroup-add-modal',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgForOf
  ],
  templateUrl: './entrygroup-add-modal.component.html',
  styleUrl: './entrygroup-add-modal.component.css'
})
export class EntrygroupAddModalComponent {
  @ViewChild('addEntryGroupSelect') selectElement!: ElementRef<HTMLSelectElement>;

  protected groups: EntrygroupDto[] = [];
  @Input() isIntake!: boolean;

  constructor(private readonly groupProvider: GroupProviderService) {
    groupProvider.groups.subscribe(g => this.groups = g);
  }

  addEntrygroup(event: Event) {
    event.preventDefault();

    console.log(this.selectElement.nativeElement.value);
  }
}
