import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EPreguntaCerradaOpcionComponent } from '../list/e-pregunta-cerrada-opcion.component';
import { EPreguntaCerradaOpcionDetailComponent } from '../detail/e-pregunta-cerrada-opcion-detail.component';
import { EPreguntaCerradaOpcionUpdateComponent } from '../update/e-pregunta-cerrada-opcion-update.component';
import { EPreguntaCerradaOpcionRoutingResolveService } from './e-pregunta-cerrada-opcion-routing-resolve.service';

const ePreguntaCerradaOpcionRoute: Routes = [
  {
    path: '',
    component: EPreguntaCerradaOpcionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EPreguntaCerradaOpcionDetailComponent,
    resolve: {
      ePreguntaCerradaOpcion: EPreguntaCerradaOpcionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EPreguntaCerradaOpcionUpdateComponent,
    resolve: {
      ePreguntaCerradaOpcion: EPreguntaCerradaOpcionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EPreguntaCerradaOpcionUpdateComponent,
    resolve: {
      ePreguntaCerradaOpcion: EPreguntaCerradaOpcionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ePreguntaCerradaOpcionRoute)],
  exports: [RouterModule],
})
export class EPreguntaCerradaOpcionRoutingModule {}
