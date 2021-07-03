import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarioEncuesta, UsuarioEncuesta } from '../usuario-encuesta.model';
import { UsuarioEncuestaService } from '../service/usuario-encuesta.service';

@Injectable({ providedIn: 'root' })
export class UsuarioEncuestaRoutingResolveService implements Resolve<IUsuarioEncuesta> {
  constructor(protected service: UsuarioEncuestaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUsuarioEncuesta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((usuarioEncuesta: HttpResponse<UsuarioEncuesta>) => {
          if (usuarioEncuesta.body) {
            return of(usuarioEncuesta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UsuarioEncuesta());
  }
}
