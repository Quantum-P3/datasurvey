import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParametroAplicacion, ParametroAplicacion } from '../parametro-aplicacion.model';
import { ParametroAplicacionService } from '../service/parametro-aplicacion.service';

@Injectable({ providedIn: 'root' })
export class ParametroAplicacionRoutingResolveService implements Resolve<IParametroAplicacion> {
  constructor(protected service: ParametroAplicacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParametroAplicacion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((parametroAplicacion: HttpResponse<ParametroAplicacion>) => {
          if (parametroAplicacion.body) {
            return of(parametroAplicacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ParametroAplicacion());
  }
}
