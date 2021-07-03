import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UsuarioEncuestaComponent } from '../list/usuario-encuesta.component';
import { UsuarioEncuestaDetailComponent } from '../detail/usuario-encuesta-detail.component';
import { UsuarioEncuestaUpdateComponent } from '../update/usuario-encuesta-update.component';
import { UsuarioEncuestaRoutingResolveService } from './usuario-encuesta-routing-resolve.service';

const usuarioEncuestaRoute: Routes = [
  {
    path: '',
    component: UsuarioEncuestaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UsuarioEncuestaDetailComponent,
    resolve: {
      usuarioEncuesta: UsuarioEncuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UsuarioEncuestaUpdateComponent,
    resolve: {
      usuarioEncuesta: UsuarioEncuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UsuarioEncuestaUpdateComponent,
    resolve: {
      usuarioEncuesta: UsuarioEncuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(usuarioEncuestaRoute)],
  exports: [RouterModule],
})
export class UsuarioEncuestaRoutingModule {}
