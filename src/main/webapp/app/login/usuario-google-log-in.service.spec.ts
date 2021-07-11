import { TestBed } from '@angular/core/testing';

import { UsuarioGoogleLogInService } from './usuario-google-log-in.service';

describe('UsuarioGoogleLogInService', () => {
  let service: UsuarioGoogleLogInService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuarioGoogleLogInService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
