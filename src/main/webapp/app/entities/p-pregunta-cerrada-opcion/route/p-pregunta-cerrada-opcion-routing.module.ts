import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PPreguntaCerradaOpcionComponent } from '../list/p-pregunta-cerrada-opcion.component';
import { PPreguntaCerradaOpcionDetailComponent } from '../detail/p-pregunta-cerrada-opcion-detail.component';
import { PPreguntaCerradaOpcionUpdateComponent } from '../update/p-pregunta-cerrada-opcion-update.component';
import { PPreguntaCerradaOpcionRoutingResolveService } from './p-pregunta-cerrada-opcion-routing-resolve.service';

const pPreguntaCerradaOpcionRoute: Routes = [
  {
    path: '',
    component: PPreguntaCerradaOpcionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PPreguntaCerradaOpcionDetailComponent,
    resolve: {
      pPreguntaCerradaOpcion: PPreguntaCerradaOpcionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PPreguntaCerradaOpcionUpdateComponent,
    resolve: {
      pPreguntaCerradaOpcion: PPreguntaCerradaOpcionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PPreguntaCerradaOpcionUpdateComponent,
    resolve: {
      pPreguntaCerradaOpcion: PPreguntaCerradaOpcionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pPreguntaCerradaOpcionRoute)],
  exports: [RouterModule],
})
export class PPreguntaCerradaOpcionRoutingModule {}
