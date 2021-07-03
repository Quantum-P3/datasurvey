import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UsuarioEncuestaComponent } from './list/usuario-encuesta.component';
import { UsuarioEncuestaDetailComponent } from './detail/usuario-encuesta-detail.component';
import { UsuarioEncuestaUpdateComponent } from './update/usuario-encuesta-update.component';
import { UsuarioEncuestaDeleteDialogComponent } from './delete/usuario-encuesta-delete-dialog.component';
import { UsuarioEncuestaRoutingModule } from './route/usuario-encuesta-routing.module';

@NgModule({
  imports: [SharedModule, UsuarioEncuestaRoutingModule],
  declarations: [
    UsuarioEncuestaComponent,
    UsuarioEncuestaDetailComponent,
    UsuarioEncuestaUpdateComponent,
    UsuarioEncuestaDeleteDialogComponent,
  ],
  entryComponents: [UsuarioEncuestaDeleteDialogComponent],
})
export class UsuarioEncuestaModule {}
