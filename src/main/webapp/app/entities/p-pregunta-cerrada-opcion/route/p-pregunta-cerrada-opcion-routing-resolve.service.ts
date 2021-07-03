import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPPreguntaCerradaOpcion, PPreguntaCerradaOpcion } from '../p-pregunta-cerrada-opcion.model';
import { PPreguntaCerradaOpcionService } from '../service/p-pregunta-cerrada-opcion.service';

@Injectable({ providedIn: 'root' })
export class PPreguntaCerradaOpcionRoutingResolveService implements Resolve<IPPreguntaCerradaOpcion> {
  constructor(protected service: PPreguntaCerradaOpcionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPPreguntaCerradaOpcion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pPreguntaCerradaOpcion: HttpResponse<PPreguntaCerradaOpcion>) => {
          if (pPreguntaCerradaOpcion.body) {
            return of(pPreguntaCerradaOpcion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PPreguntaCerradaOpcion());
  }
}
