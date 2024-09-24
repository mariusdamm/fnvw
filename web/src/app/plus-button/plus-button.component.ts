import {Component, ElementRef, ViewChild} from '@angular/core';
import {MonthAddModalComponent} from "../screens/entry-screen/month-add-modal/month-add-modal.component";

@Component({
  selector: 'app-plus-button',
  standalone: true,
  imports: [
    MonthAddModalComponent
  ],
  templateUrl: './plus-button.component.html',
  styleUrl: './plus-button.component.css'
})
export class PlusButtonComponent {

  @ViewChild('plusIcon') plusIcon!: ElementRef;

  toggleIconRotate() {
    if (this.plusIcon.nativeElement.classList.contains('svg-rotate')){
      this.plusIcon.nativeElement.classList.remove('svg-rotate');
      this.plusIcon.nativeElement.classList.add('svg-unrotate');
    }
    else {
      this.plusIcon.nativeElement.classList.remove('svg-unrotate');
      this.plusIcon.nativeElement.classList.add('svg-rotate');
    }
  }
}
