import { userManagementRoute } from './../../admin/user-management/user-management.route';
import { Component, OnInit, AfterViewInit, AfterViewChecked, AfterContentInit } from '@angular/core';
import { RouteInfo, ADMIN_ROUTES, USER_ROUTES } from './sidebar.constants';
import { VERSION } from 'app/app.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { Router } from '@angular/router';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { UsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent {
  public menuAdmin: RouteInfo[] = ADMIN_ROUTES;
  public menuUser: RouteInfo[] = USER_ROUTES;

  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;

  usuarioExtra: UsuarioExtra | null = null;

  constructor(
    private loginService: LoginService,
    private sessionStorageService: SessionStorageService,
    private localStorageService: LocalStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
    private usuarioExtraService: UsuarioExtraService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION;
    }
  }

  isNotMobileMenu() {
    if (window.outerWidth > 991) {
      return false;
    }
    return true;
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    // Get jhi_user and usuario_extra information
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      if (account !== null) {
        this.usuarioExtraService.find(account.id).subscribe(usuarioExtra => {
          this.usuarioExtra = usuarioExtra.body;
          this.usuarioExtra!.nombre =
            usuarioExtra.body!.nombre!.trim().split(' ')[0] + ' ' + usuarioExtra.body!.nombre!.trim().split(' ')[1];
        });
      }
    });
  }

  ngAfterViewInit() {}

  isAdmin(): boolean {
    return this.accountService.hasAnyAuthority('ROLE_ADMIN');
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
    this.localStorageService.clear('IsGoogle');
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
}
