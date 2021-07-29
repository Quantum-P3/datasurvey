import { Component } from '@angular/core';
import { Account } from '../../core/auth/account.model';
import { takeUntil } from 'rxjs/operators';
import { AccountService } from '../../core/auth/account.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent {
  account: Account | null = null;
  notAccount: boolean = true;
  private readonly destroy$ = new Subject<void>();

  constructor(protected accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        if (account !== null) {
          this.account = account;
          this.notAccount = false;
        } else {
          this.notAccount = true;
        }
      });
  }
}
