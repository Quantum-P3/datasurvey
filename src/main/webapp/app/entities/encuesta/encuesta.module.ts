import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EncuestaComponent } from './list/encuesta.component';
import { EncuestaDetailComponent } from './detail/encuesta-detail.component';
import { EncuestaUpdateComponent } from './update/encuesta-update.component';
import { EncuestaDeleteDialogComponent } from './delete/encuesta-delete-dialog.component';
import { EncuestaRoutingModule } from './route/encuesta-routing.module';

@NgModule({
  imports: [SharedModule, EncuestaRoutingModule],
  declarations: [EncuestaComponent, EncuestaDetailComponent, EncuestaUpdateComponent, EncuestaDeleteDialogComponent],
  entryComponents: [EncuestaDeleteDialogComponent],
})
export class EncuestaModule {}
