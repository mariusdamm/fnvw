import {Component, ElementRef, Input, ViewChild} from '@angular/core';
import {EntrygroupDto} from "../../../dtos/entrygroup-dto";
import {EntryCreateModalComponent} from "../entry-create-modal/entry-create-modal.component";
import {UtilService} from "../../../services/util.service";
import {NgForOf, NgIf} from "@angular/common";

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

  toggleIconRotate() {
    if (this.collapseIcon.nativeElement.classList.contains('svg-rotate')){
      this.collapseIcon.nativeElement.classList.remove('svg-rotate');
      this.collapseIcon.nativeElement.classList.add('svg-unrotate');
    }
    else {
      this.collapseIcon.nativeElement.classList.remove('svg-unrotate');
      this.collapseIcon.nativeElement.classList.add('svg-rotate');
    }
  }
}
