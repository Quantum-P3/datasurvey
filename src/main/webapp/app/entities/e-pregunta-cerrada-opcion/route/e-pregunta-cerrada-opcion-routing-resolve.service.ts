import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEPreguntaCerradaOpcion, EPreguntaCerradaOpcion } from '../e-pregunta-cerrada-opcion.model';
import { EPreguntaCerradaOpcionService } from '../service/e-pregunta-cerrada-opcion.service';

@Injectable({ providedIn: 'root' })
export class EPreguntaCerradaOpcionRoutingResolveService implements Resolve<IEPreguntaCerradaOpcion> {
  constructor(protected service: EPreguntaCerradaOpcionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEPreguntaCerradaOpcion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ePreguntaCerradaOpcion: HttpResponse<EPreguntaCerradaOpcion>) => {
          if (ePreguntaCerradaOpcion.body) {
            return of(ePreguntaCerradaOpcion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EPreguntaCerradaOpcion());
  }
}
