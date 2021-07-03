import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPPreguntaCerrada, PPreguntaCerrada } from '../p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from '../service/p-pregunta-cerrada.service';

@Injectable({ providedIn: 'root' })
export class PPreguntaCerradaRoutingResolveService implements Resolve<IPPreguntaCerrada> {
  constructor(protected service: PPreguntaCerradaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPPreguntaCerrada> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pPreguntaCerrada: HttpResponse<PPreguntaCerrada>) => {
          if (pPreguntaCerrada.body) {
            return of(pPreguntaCerrada.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PPreguntaCerrada());
  }
}
