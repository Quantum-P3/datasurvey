import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EPreguntaCerradaComponent } from '../list/e-pregunta-cerrada.component';
import { EPreguntaCerradaDetailComponent } from '../detail/e-pregunta-cerrada-detail.component';
import { EPreguntaCerradaUpdateComponent } from '../update/e-pregunta-cerrada-update.component';
import { EPreguntaCerradaRoutingResolveService } from './e-pregunta-cerrada-routing-resolve.service';

const ePreguntaCerradaRoute: Routes = [
  {
    path: '',
    component: EPreguntaCerradaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EPreguntaCerradaDetailComponent,
    resolve: {
      ePreguntaCerrada: EPreguntaCerradaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EPreguntaCerradaUpdateComponent,
    resolve: {
      ePreguntaCerrada: EPreguntaCerradaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EPreguntaCerradaUpdateComponent,
    resolve: {
      ePreguntaCerrada: EPreguntaCerradaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ePreguntaCerradaRoute)],
  exports: [RouterModule],
})
export class EPreguntaCerradaRoutingModule {}
