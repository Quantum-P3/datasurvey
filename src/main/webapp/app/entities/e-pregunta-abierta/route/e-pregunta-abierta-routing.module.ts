import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EPreguntaAbiertaComponent } from '../list/e-pregunta-abierta.component';
import { EPreguntaAbiertaDetailComponent } from '../detail/e-pregunta-abierta-detail.component';
import { EPreguntaAbiertaUpdateComponent } from '../update/e-pregunta-abierta-update.component';
import { EPreguntaAbiertaRoutingResolveService } from './e-pregunta-abierta-routing-resolve.service';

const ePreguntaAbiertaRoute: Routes = [
  {
    path: '',
    component: EPreguntaAbiertaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EPreguntaAbiertaDetailComponent,
    resolve: {
      ePreguntaAbierta: EPreguntaAbiertaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EPreguntaAbiertaUpdateComponent,
    resolve: {
      ePreguntaAbierta: EPreguntaAbiertaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EPreguntaAbiertaUpdateComponent,
    resolve: {
      ePreguntaAbierta: EPreguntaAbiertaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ePreguntaAbiertaRoute)],
  exports: [RouterModule],
})
export class EPreguntaAbiertaRoutingModule {}
