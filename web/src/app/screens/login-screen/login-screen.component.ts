import {Component, ElementRef, ViewChild} from '@angular/core';
import {UtilService} from '../../services/util.service';
import {LoginDto} from "../../dtos/login-dto";
import {AuthService} from "../../services/auth.service";
import {AxiosService} from "../../services/axios.service";
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";

declare let bootstrap: any;

@Component({
  selector: 'app-login-screen',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './login-screen.component.html',
  styleUrl: './login-screen.component.css'
})
export class LoginScreenComponent {

  @ViewChild('usernameInput') private usernameInput!: ElementRef<HTMLInputElement>;
  @ViewChild('passwordInput') private passwordInput!: ElementRef<HTMLInputElement>;

  constructor(private utilService: UtilService,
              private jwtService: AuthService,
              private axiosService: AxiosService,
              private router: Router,
  ) {
  }

  loginUser(event: Event) {
    event.preventDefault();

    const username = this.usernameInput.nativeElement.value.trim();
    const password = this.passwordInput.nativeElement.value.trim();

    if (username === '') {
      this.utilService.highlightInvalidInput(this.usernameInput.nativeElement);
    }
    if (password === '') {
      this.utilService.highlightInvalidInput(this.passwordInput.nativeElement);
    }
    if (username === '' || password === '') {
      return;
    }

    let loginDto: LoginDto = new LoginDto(username, password);

    this.axiosService.requestWithoutToken(
      "POST",
      "/auth/login",
      loginDto
    ).then(response => {
      if (response.status > 299)
        throw new Error('Network response was not ok');
      return response.data;
    }).then(loginResponse => {
      this.jwtService.saveJwtToken(loginResponse.token);
      this.router.navigate(['/dashboard']).then(() => false);
    }).catch(error => {
      console.error('Fetch error:', error);
      const bsCollapse = new bootstrap.Collapse('#loginDangerCollapse', {});
      bsCollapse.show();
      setTimeout(() => bsCollapse.hide(), 3000);
    });
  }
}
