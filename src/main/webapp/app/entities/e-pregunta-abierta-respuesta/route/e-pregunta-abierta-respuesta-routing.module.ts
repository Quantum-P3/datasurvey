import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EPreguntaAbiertaRespuestaComponent } from '../list/e-pregunta-abierta-respuesta.component';
import { EPreguntaAbiertaRespuestaDetailComponent } from '../detail/e-pregunta-abierta-respuesta-detail.component';
import { EPreguntaAbiertaRespuestaUpdateComponent } from '../update/e-pregunta-abierta-respuesta-update.component';
import { EPreguntaAbiertaRespuestaRoutingResolveService } from './e-pregunta-abierta-respuesta-routing-resolve.service';

const ePreguntaAbiertaRespuestaRoute: Routes = [
  {
    path: '',
    component: EPreguntaAbiertaRespuestaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EPreguntaAbiertaRespuestaDetailComponent,
    resolve: {
      ePreguntaAbiertaRespuesta: EPreguntaAbiertaRespuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EPreguntaAbiertaRespuestaUpdateComponent,
    resolve: {
      ePreguntaAbiertaRespuesta: EPreguntaAbiertaRespuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EPreguntaAbiertaRespuestaUpdateComponent,
    resolve: {
      ePreguntaAbiertaRespuesta: EPreguntaAbiertaRespuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ePreguntaAbiertaRespuestaRoute)],
  exports: [RouterModule],
})
export class EPreguntaAbiertaRespuestaRoutingModule {}
