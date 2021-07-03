import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEncuesta, Encuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';

@Injectable({ providedIn: 'root' })
export class EncuestaRoutingResolveService implements Resolve<IEncuesta> {
  constructor(protected service: EncuestaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEncuesta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((encuesta: HttpResponse<Encuesta>) => {
          if (encuesta.body) {
            return of(encuesta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Encuesta());
  }
}
