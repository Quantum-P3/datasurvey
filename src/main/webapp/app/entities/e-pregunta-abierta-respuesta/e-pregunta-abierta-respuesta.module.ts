import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EPreguntaAbiertaRespuestaComponent } from './list/e-pregunta-abierta-respuesta.component';
import { EPreguntaAbiertaRespuestaDetailComponent } from './detail/e-pregunta-abierta-respuesta-detail.component';
import { EPreguntaAbiertaRespuestaUpdateComponent } from './update/e-pregunta-abierta-respuesta-update.component';
import { EPreguntaAbiertaRespuestaDeleteDialogComponent } from './delete/e-pregunta-abierta-respuesta-delete-dialog.component';
import { EPreguntaAbiertaRespuestaRoutingModule } from './route/e-pregunta-abierta-respuesta-routing.module';

@NgModule({
  imports: [SharedModule, EPreguntaAbiertaRespuestaRoutingModule],
  declarations: [
    EPreguntaAbiertaRespuestaComponent,
    EPreguntaAbiertaRespuestaDetailComponent,
    EPreguntaAbiertaRespuestaUpdateComponent,
    EPreguntaAbiertaRespuestaDeleteDialogComponent,
  ],
  entryComponents: [EPreguntaAbiertaRespuestaDeleteDialogComponent],
})
export class EPreguntaAbiertaRespuestaModule {}
