import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UsuarioPlantillasDetailComponent } from '../detail/usuario-plantillas-detail.component';

import { UsuarioPlantillasComponent } from '../list/usuario-plantillas.component';
import { UsuarioPlantillasRoutingResolveService } from './usuario-plantillas-routing-resolve.service';

const USUARIO_PLANTILLAS_ROUTE: Routes = [
  {
    path: '',
    component: UsuarioPlantillasComponent,
    data: {
      pageTitle: 'dataSurveyApp.usuarioExtra.plantillas.title',
    },
  },
  {
    path: ':id/preview',
    component: UsuarioPlantillasDetailComponent,
    resolve: {
      plantilla: UsuarioPlantillasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(USUARIO_PLANTILLAS_ROUTE)],
  exports: [RouterModule],
})
export class UsuarioPlantillasRoutingModule {}
