import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { SocialAuthService, SocialUser } from 'angularx-social-login';
import { GoogleLoginProvider } from 'angularx-social-login';
import { RegisterService } from '../account/register/register.service';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse } from '@angular/common/http';
import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from '../config/error.constants';

@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;

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
    private translateService: TranslateService
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
      () => (this.authenticationError = true)
      /*this.registerService
          .save({
            login: this.user.email,
            email: this.user.email,
            password: this.user.id,
            langKey: this.translateService.currentLang,
            name: this.user.name,
            profileIcon: this.randomProfilePic(),
            isAdmin: 0,
          })
          .subscribe(
            () => (this.success = true),
            response => this.processError(response)
          ) */ //console.log("Usuario no existe")
    );
  }

  randomProfilePic(): number {
    return Math.floor(Math.random() * (28 - 1 + 1)) + 1;
  }

  processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }

  refreshToken(): void {
    this.authService.refreshAuthToken(GoogleLoginProvider.PROVIDER_ID);
  }

  activateGoogle(): void {
    this.registerService
      .save({
        login: this.user.email,
        email: this.user.email,
        password: this.user.id,
        langKey: this.translateService.currentLang,
        name: this.user.name,
        profileIcon: this.randomProfilePic(),
        isAdmin: 0,
      })
      .subscribe(
        () => (this.success = true),
        response => this.processError(response)
      );
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
