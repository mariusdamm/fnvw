import {Component, ElementRef, Input, ViewChild} from '@angular/core';
import {NgClass, NgForOf} from "@angular/common";
import {EntrygroupCreateModalComponent} from "../entrygroup-create-modal/entrygroup-create-modal.component";
import {EntrygroupDto} from "../../../dtos/entrygroup-dto";
import {toggleIconRotate} from "../../../util";

@Component({
  selector: 'app-entrygroup-create-card',
  standalone: true,
  imports: [
    NgForOf,
    EntrygroupCreateModalComponent,
    NgClass
  ],
  templateUrl: './entrygroup-create-card.component.html',
  styleUrl: './entrygroup-create-card.component.css'
})
export class EntrygroupCreateCardComponent {
  @Input() groups: EntrygroupDto[] = []
  @Input() isIntake!: boolean;
  @ViewChild('collapseIcon') collapseIcon!: ElementRef;

  constructor() {
  }

  protected readonly toggleIconRotate = toggleIconRotate;
}
