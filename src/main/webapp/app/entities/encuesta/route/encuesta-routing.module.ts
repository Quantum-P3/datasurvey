import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EncuestaComponent } from '../list/encuesta.component';
import { EncuestaDetailComponent } from '../detail/encuesta-detail.component';
import { EncuestaUpdateComponent } from '../update/encuesta-update.component';
import { EncuestaRoutingResolveService } from './encuesta-routing-resolve.service';

const encuestaRoute: Routes = [
  {
    path: '',
    component: EncuestaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EncuestaDetailComponent,
    resolve: {
      encuesta: EncuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EncuestaUpdateComponent,
    resolve: {
      encuesta: EncuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/preview',
    component: EncuestaUpdateComponent,
    resolve: {
      encuesta: EncuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(encuestaRoute)],
  exports: [RouterModule],
})
export class EncuestaRoutingModule {}
