import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPPreguntaAbierta, PPreguntaAbierta } from '../p-pregunta-abierta.model';
import { PPreguntaAbiertaService } from '../service/p-pregunta-abierta.service';

@Injectable({ providedIn: 'root' })
export class PPreguntaAbiertaRoutingResolveService implements Resolve<IPPreguntaAbierta> {
  constructor(protected service: PPreguntaAbiertaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPPreguntaAbierta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pPreguntaAbierta: HttpResponse<PPreguntaAbierta>) => {
          if (pPreguntaAbierta.body) {
            return of(pPreguntaAbierta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PPreguntaAbierta());
  }
}
