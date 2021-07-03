import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEPreguntaAbierta, EPreguntaAbierta } from '../e-pregunta-abierta.model';
import { EPreguntaAbiertaService } from '../service/e-pregunta-abierta.service';

@Injectable({ providedIn: 'root' })
export class EPreguntaAbiertaRoutingResolveService implements Resolve<IEPreguntaAbierta> {
  constructor(protected service: EPreguntaAbiertaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEPreguntaAbierta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ePreguntaAbierta: HttpResponse<EPreguntaAbierta>) => {
          if (ePreguntaAbierta.body) {
            return of(ePreguntaAbierta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EPreguntaAbierta());
  }
}
