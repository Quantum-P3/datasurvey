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
import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE, USER_IS_SUSPENDED } from '../config/error.constants';
import { LocalStorageService } from 'ngx-webstorage';
import { UsuarioExtra } from '../entities/usuario-extra/usuario-extra.model';
import { Account } from '../core/auth/account.model';

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
  userSuspended = false;
  imprimir = false;

  loginForm = this.fb.group({
    username: [null, [Validators.required, Validators.email, Validators.maxLength(254)]],
    password: [null, [Validators.required, Validators.minLength(8), Validators.maxLength(50)]],
    rememberMe: [false],
  });

  user: SocialUser = new SocialUser();
  loggedIn: boolean = false;
  success = false;

  constructor(
    private localStorageService: LocalStorageService,
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router,
    private fb: FormBuilder,
    private authService: SocialAuthService,
    private registerService: RegisterService,
    private translateService: TranslateService
  ) {}

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['/pagina-principal']);
      }
    });
  }

  ngAfterViewInit(): void {
    // this.username.nativeElement.focus();
  }

  //Inicio Google
  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID).then(() => {
      this.authService.authState.subscribe(user => {
        this.user = user;
        this.loggedIn = user != null;

        /* console.log('correo: ' + user.email);
         console.log('correo: ' + user.name);
         console.log('ID: ' + this.user.id);*/

        this.authenticacionGoogle();
      });
    });
  }

  authenticacionGoogle(): void {
    this.error = false;
    this.userSuspended = false;

    this.loginService.login({ username: this.user.email, password: this.user.id, rememberMe: false }).subscribe(
      () => {
        this.authenticationError = false;
        if (!this.router.getCurrentNavigation()) {
          this.localStorageService.store('IsGoogle', 'true');
          // There were no routing during login (eg from navigationToStoredUrl)
          this.router.navigate(['/pagina-principal']);
        }
      },
      response => {
        if (response.status == 401 && response.error.detail == 'Bad credentials') {
          this.activateGoogle();
        } else {
          this.processError(response);
        }
      }
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
    } else if (response.status === 401) {
      this.userSuspended = true;
    } else {
      this.error = true;
    }
  }

  refreshToken(): void {
    this.authService.refreshAuthToken(GoogleLoginProvider.PROVIDER_ID);
  }

  activateGoogle(): void {
    this.error = false;
    this.userSuspended = false;

    this.registerService
      .save({
        login: this.user.email,
        email: this.user.email,
        password: this.user.id,
        langKey: this.translateService.currentLang,
        name: this.user.name,
        firstName: 'IsGoogle',
        profileIcon: this.randomProfilePic(),
        isAdmin: 0,
        isGoogle: 1,
      })
      .subscribe(
        () => {
          this.success = true;
          this.authenticacionGoogle();
        },
        response => this.processError(response)
      );
  }

  login(): void {
    this.error = false;
    this.userSuspended = false;
    this.loginService
      .login({
        username: this.loginForm.get('username')!.value,
        password: this.loginForm.get('password')!.value,
        rememberMe: this.loginForm.get('rememberMe')!.value,
      })
      .subscribe(
        value => {
          /*if (value?.activated == false){
              this.userSuspended = true;

              console.log(value.activated)
            }else {*/
          this.authenticationError = false;
          if (!this.router.getCurrentNavigation()) {
            // There were no routing during login (eg from navigationToStoredUrl)
            this.router.navigate(['/pagina-principal']);
          }
          // }
        },
        response => {
          if (response.status == 401 && response.error.detail == 'Bad credentials') {
            this.error = true;
          } else {
            this.processError(response);
          }
        }
      );
  }
}
