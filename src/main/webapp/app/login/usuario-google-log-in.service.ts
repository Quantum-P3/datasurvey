import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UsuarioGoogleLogInService {
  constructor(private auth2: gapi.auth2.GoogleAuth, private subject: ReplaySubject<gapi.auth2.GoogleUser>) {
    gapi.load('auth2', () => {
      this.auth2 = gapi.auth2.init({
        client_id: '178178891217-b517thad8f15d4at2vk2410v7a09dcvt.apps.googleusercontent.com',
      });
    });
  }

  public sigIn() {
    this.auth2
      .signIn({
        //
      })
      .then(user => {
        this.subject.next(user);
      })
      .catch(() => {
        this.subject.next(); //NULL
      });
  }

  public observable(): Observable<gapi.auth2.GoogleUser> {
    return this.subject.asObservable();
  }
}
