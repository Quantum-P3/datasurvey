import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PPreguntaAbiertaComponent } from '../list/p-pregunta-abierta.component';
import { PPreguntaAbiertaDetailComponent } from '../detail/p-pregunta-abierta-detail.component';
import { PPreguntaAbiertaUpdateComponent } from '../update/p-pregunta-abierta-update.component';
import { PPreguntaAbiertaRoutingResolveService } from './p-pregunta-abierta-routing-resolve.service';

const pPreguntaAbiertaRoute: Routes = [
  {
    path: '',
    component: PPreguntaAbiertaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PPreguntaAbiertaDetailComponent,
    resolve: {
      pPreguntaAbierta: PPreguntaAbiertaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PPreguntaAbiertaUpdateComponent,
    resolve: {
      pPreguntaAbierta: PPreguntaAbiertaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PPreguntaAbiertaUpdateComponent,
    resolve: {
      pPreguntaAbierta: PPreguntaAbiertaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pPreguntaAbiertaRoute)],
  exports: [RouterModule],
})
export class PPreguntaAbiertaRoutingModule {}
