import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PPreguntaCerradaComponent } from '../list/p-pregunta-cerrada.component';
import { PPreguntaCerradaDetailComponent } from '../detail/p-pregunta-cerrada-detail.component';
import { PPreguntaCerradaUpdateComponent } from '../update/p-pregunta-cerrada-update.component';
import { PPreguntaCerradaRoutingResolveService } from './p-pregunta-cerrada-routing-resolve.service';

const pPreguntaCerradaRoute: Routes = [
  {
    path: '',
    component: PPreguntaCerradaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PPreguntaCerradaDetailComponent,
    resolve: {
      pPreguntaCerrada: PPreguntaCerradaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PPreguntaCerradaUpdateComponent,
    resolve: {
      pPreguntaCerrada: PPreguntaCerradaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PPreguntaCerradaUpdateComponent,
    resolve: {
      pPreguntaCerrada: PPreguntaCerradaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pPreguntaCerradaRoute)],
  exports: [RouterModule],
})
export class PPreguntaCerradaRoutingModule {}
