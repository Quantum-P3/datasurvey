import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEPreguntaAbiertaRespuesta, EPreguntaAbiertaRespuesta } from '../e-pregunta-abierta-respuesta.model';
import { EPreguntaAbiertaRespuestaService } from '../service/e-pregunta-abierta-respuesta.service';

@Injectable({ providedIn: 'root' })
export class EPreguntaAbiertaRespuestaRoutingResolveService implements Resolve<IEPreguntaAbiertaRespuesta> {
  constructor(protected service: EPreguntaAbiertaRespuestaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEPreguntaAbiertaRespuesta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ePreguntaAbiertaRespuesta: HttpResponse<EPreguntaAbiertaRespuesta>) => {
          if (ePreguntaAbiertaRespuesta.body) {
            return of(ePreguntaAbiertaRespuesta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EPreguntaAbiertaRespuesta());
  }
}
