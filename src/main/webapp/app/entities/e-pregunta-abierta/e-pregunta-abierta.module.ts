import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EPreguntaAbiertaComponent } from './list/e-pregunta-abierta.component';
import { EPreguntaAbiertaDetailComponent } from './detail/e-pregunta-abierta-detail.component';
import { EPreguntaAbiertaUpdateComponent } from './update/e-pregunta-abierta-update.component';
import { EPreguntaAbiertaDeleteDialogComponent } from './delete/e-pregunta-abierta-delete-dialog.component';
import { EPreguntaAbiertaRoutingModule } from './route/e-pregunta-abierta-routing.module';

@NgModule({
  imports: [SharedModule, EPreguntaAbiertaRoutingModule],
  declarations: [
    EPreguntaAbiertaComponent,
    EPreguntaAbiertaDetailComponent,
    EPreguntaAbiertaUpdateComponent,
    EPreguntaAbiertaDeleteDialogComponent,
  ],
  entryComponents: [EPreguntaAbiertaDeleteDialogComponent],
})
export class EPreguntaAbiertaModule {}
