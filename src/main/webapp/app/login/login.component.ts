import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { SocialAuthService, SocialUser } from 'angularx-social-login';
import { GoogleLoginProvider } from 'angularx-social-login';
import { RegisterService } from '../account/register/register.service';
import { TranslateService } from '@ngx-translate/core';
import { RegisterComponent } from '../account/register/register.component';

@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;

  loginForm = this.fb.group({
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    rememberMe: [false],
  });

  user: SocialUser = new SocialUser();
  loggedIn: boolean = false;
  success = false;

  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router,
    private fb: FormBuilder,
    private authService: SocialAuthService,
    private registerService: RegisterService,
    private translateService: TranslateService,
    private registerAuth: RegisterComponent
  ) {}

  ngOnInit(): void {
    //Servicio para verificar si el usuario se encuentra loggeado
    this.authService.authState.subscribe(user => {
      this.user = user;
      this.loggedIn = user != null;

      console.log('correo: ' + user.email);
      console.log('correo: ' + user.name);
      console.log('ID: ' + this.user.id);

      this.authenticacionGoogle();
    });

    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });
  }

  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }

  //Inicio Google
  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  authenticacionGoogle(): void {
    this.loginService.login({ username: this.user.email, password: this.user.id, rememberMe: true }).subscribe(
      () => {
        this.authenticationError = false;
        if (!this.router.getCurrentNavigation()) {
          // There were no routing during login (eg from navigationToStoredUrl)
          this.router.navigate(['']);
        }
        console.log('SI existe');
      },
      () =>
        this.registerService
          .save({
            login: this.user.email,
            email: this.user.email,
            password: this.user.idToken,
            langKey: this.translateService.currentLang,
            name: this.user.name,
            profileIcon: this.randomProfilePic(),
          })
          .subscribe(
            () => (this.success = true),
            response => this.registerAuth.processError(response)
          ) //console.log("Numero random: " + this.randomProfilePic())
    );
  }

  randomProfilePic(): number {
    return Math.floor(Math.random() * (28 - 1 + 1)) + 1;
  }

  refreshToken(): void {
    this.authService.refreshAuthToken(GoogleLoginProvider.PROVIDER_ID);
  }

  login(): void {
    this.loginService
      .login({
        username: this.loginForm.get('username')!.value,
        password: this.loginForm.get('password')!.value,
        rememberMe: this.loginForm.get('rememberMe')!.value,
      })
      .subscribe(
        () => {
          this.authenticationError = false;
          if (!this.router.getCurrentNavigation()) {
            // There were no routing during login (eg from navigationToStoredUrl)
            this.router.navigate(['']);
          }
        },
        () => (this.authenticationError = true)
      );
  }
}
