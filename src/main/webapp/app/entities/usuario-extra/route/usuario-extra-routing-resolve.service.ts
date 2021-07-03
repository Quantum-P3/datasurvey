import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarioExtra, UsuarioExtra } from '../usuario-extra.model';
import { UsuarioExtraService } from '../service/usuario-extra.service';

@Injectable({ providedIn: 'root' })
export class UsuarioExtraRoutingResolveService implements Resolve<IUsuarioExtra> {
  constructor(protected service: UsuarioExtraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUsuarioExtra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((usuarioExtra: HttpResponse<UsuarioExtra>) => {
          if (usuarioExtra.body) {
            return of(usuarioExtra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UsuarioExtra());
  }
}
