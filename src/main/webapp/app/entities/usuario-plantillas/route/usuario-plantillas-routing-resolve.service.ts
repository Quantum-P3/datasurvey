import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { IPlantilla, Plantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';

@Injectable({ providedIn: 'root' })
export class UsuarioPlantillasRoutingResolveService implements Resolve<IPlantilla> {
  constructor(protected service: PlantillaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlantilla> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((plantilla: HttpResponse<Plantilla>) => {
          if (plantilla.body) {
            return of(plantilla.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Plantilla());
  }
}
