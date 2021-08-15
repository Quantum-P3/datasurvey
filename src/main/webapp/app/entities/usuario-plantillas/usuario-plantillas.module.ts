import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioPlantillasComponent } from './list/usuario-plantillas.component';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SharedModule } from '../../shared/shared.module';
import { UsuarioPlantillasRoutingModule } from './route/usuario-plantillas.route';
import { UsuarioPlantillasDetailComponent } from './detail/usuario-plantillas-detail.component';

@NgModule({
  imports: [CommonModule, UsuarioPlantillasRoutingModule, FontAwesomeModule, SharedModule],
  declarations: [UsuarioPlantillasComponent, UsuarioPlantillasDetailComponent],
})
export class UsuarioPlantillasModule {}
