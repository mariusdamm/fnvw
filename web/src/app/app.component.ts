import {Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {HelpModalComponent} from "./header/help-modal/help-modal.component";
import {NameDropdownComponent} from "./header/name-dropdown/name-dropdown.component";
import {AuthService} from "./services/auth.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgOptimizedImage, HelpModalComponent, RouterLink, NameDropdownComponent, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {

  isLoggedIn: boolean | undefined;
  private loggedInSubscription?: Subscription;

  constructor(
    private readonly authService: AuthService,
  ) {
  }

  ngOnInit() {
    this.loggedInSubscription = this.authService.isLoggedIn
      .subscribe(isLoggedIn => this.isLoggedIn = isLoggedIn);
  }

  ngOnDestroy() {
    if (this.loggedInSubscription)
      this.loggedInSubscription.unsubscribe();
  }
}
