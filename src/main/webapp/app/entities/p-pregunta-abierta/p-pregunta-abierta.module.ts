import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PPreguntaAbiertaComponent } from './list/p-pregunta-abierta.component';
import { PPreguntaAbiertaDetailComponent } from './detail/p-pregunta-abierta-detail.component';
import { PPreguntaAbiertaUpdateComponent } from './update/p-pregunta-abierta-update.component';
import { PPreguntaAbiertaDeleteDialogComponent } from './delete/p-pregunta-abierta-delete-dialog.component';
import { PPreguntaAbiertaRoutingModule } from './route/p-pregunta-abierta-routing.module';

@NgModule({
  imports: [SharedModule, PPreguntaAbiertaRoutingModule],
  declarations: [
    PPreguntaAbiertaComponent,
    PPreguntaAbiertaDetailComponent,
    PPreguntaAbiertaUpdateComponent,
    PPreguntaAbiertaDeleteDialogComponent,
  ],
  entryComponents: [PPreguntaAbiertaDeleteDialogComponent],
})
export class PPreguntaAbiertaModule {}
