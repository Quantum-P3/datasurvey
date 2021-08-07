import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioPlantillasComponent } from './list/usuario-plantillas.component';
import { RouterModule } from '@angular/router';
import { USUARIO_PLANTILLAS_ROUTE } from './route/usuario-plantillas.route';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [UsuarioPlantillasComponent],
  imports: [CommonModule, RouterModule.forChild([USUARIO_PLANTILLAS_ROUTE]), FontAwesomeModule, SharedModule],
})
export class UsuarioPlantillasModule {}
