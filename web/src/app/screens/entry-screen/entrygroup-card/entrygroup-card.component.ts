import {Component, ElementRef, Input, ViewChild} from '@angular/core';
import {EntrygroupDto} from "../../../dtos/entrygroup-dto";
import {EntryCreateModalComponent} from "../entry-create-modal/entry-create-modal.component";
import {UtilService} from "../../../services/util.service";
import {NgForOf, NgIf} from "@angular/common";
import {toggleIconRotate} from "../../../util"

@Component({
  selector: 'app-entrygroup-card',
  standalone: true,
  imports: [
    EntryCreateModalComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './entrygroup-card.component.html',
  styleUrl: './entrygroup-card.component.css'
})
export class EntrygroupCardComponent {

  @ViewChild('collapseIcon') collapseIcon!: ElementRef;
  @Input() group?: EntrygroupDto;

  constructor(protected utilService: UtilService) {
  }

  protected readonly toggleIconRotate = toggleIconRotate;
}
