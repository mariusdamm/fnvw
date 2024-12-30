import {Component, OnDestroy, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterOutlet} from '@angular/router';
import {NgClass, NgIf, NgOptimizedImage} from "@angular/common";
import {HelpModalComponent} from "./header/help-modal/help-modal.component";
import {NameDropdownComponent} from "./header/name-dropdown/name-dropdown.component";
import {AuthService} from "./services/auth.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgOptimizedImage, HelpModalComponent, RouterLink, NameDropdownComponent, NgIf, NgClass],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {

  isLoggedIn: boolean | undefined;
  private loggedInSubscription?: Subscription;
  currentRoute: string = '';


  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = event.urlAfterRedirects;
      }
    });
  }

  isActive(route: string): boolean {
    return this.currentRoute === route;
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
