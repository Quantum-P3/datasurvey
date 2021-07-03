import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEPreguntaCerrada, EPreguntaCerrada } from '../e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from '../service/e-pregunta-cerrada.service';

@Injectable({ providedIn: 'root' })
export class EPreguntaCerradaRoutingResolveService implements Resolve<IEPreguntaCerrada> {
  constructor(protected service: EPreguntaCerradaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEPreguntaCerrada> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ePreguntaCerrada: HttpResponse<EPreguntaCerrada>) => {
          if (ePreguntaCerrada.body) {
            return of(ePreguntaCerrada.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EPreguntaCerrada());
  }
}
