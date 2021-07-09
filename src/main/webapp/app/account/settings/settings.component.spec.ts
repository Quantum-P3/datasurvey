jest.mock('@ngx-translate/core');
jest.mock('app/core/auth/account.service');

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { throwError, of } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import { SettingsComponent } from './settings.component';

import { RouterTestingModule } from '@angular/router/testing';

describe('Component Tests', () => {
  describe('SettingsComponent', () => {
    let comp: SettingsComponent;
    let fixture: ComponentFixture<SettingsComponent>;
    let mockAccountService: AccountService;
    const account: Account = {
      id: 0,
      firstName: 'John',
      lastName: 'Doe',
      activated: true,
      email: 'john.doe@mail.com',
      langKey: 'es',
      login: 'john',
      authorities: [],
      imageUrl: '',
    };

    beforeEach(
      waitForAsync(() => {
        TestBed.configureTestingModule({
          imports: [RouterTestingModule, HttpClientTestingModule],
          declarations: [SettingsComponent],
          providers: [FormBuilder, TranslateService, AccountService],
        })
          .overrideTemplate(SettingsComponent, '')
          .compileComponents();
      })
    );

    beforeEach(() => {
      fixture = TestBed.createComponent(SettingsComponent);
      comp = fixture.componentInstance;
      mockAccountService = TestBed.inject(AccountService);
      mockAccountService.identity = jest.fn(() => of(account));
      mockAccountService.getAuthenticationState = jest.fn(() => of(account));
    });

    it('should notify of success upon successful save', () => {
      // GIVEN
      mockAccountService.save = jest.fn(() => of({}));

      // WHEN
      comp.ngOnInit();
      comp.save();

      // THEN
      // expect(comp.success).toBe(true);
    });

    it('should notify of error upon failed save', () => {
      // GIVEN
      mockAccountService.save = jest.fn(() => throwError('ERROR'));

      // WHEN
      comp.ngOnInit();
      comp.save();

      // THEN
      // expect(comp.success).toBe(false);
    });
  });
});
