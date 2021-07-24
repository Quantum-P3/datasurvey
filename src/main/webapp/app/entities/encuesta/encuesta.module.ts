import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EncuestaComponent } from './list/encuesta.component';
import { EncuestaDetailComponent } from './detail/encuesta-detail.component';
import { EncuestaUpdateComponent } from './update/encuesta-update.component';
import { EncuestaDeleteDialogComponent } from './delete/encuesta-delete-dialog.component';
import { EncuestaRoutingModule } from './route/encuesta-routing.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  imports: [SharedModule, EncuestaRoutingModule, FontAwesomeModule],
  declarations: [EncuestaComponent, EncuestaDetailComponent, EncuestaUpdateComponent, EncuestaDeleteDialogComponent],
  entryComponents: [EncuestaDeleteDialogComponent],
})
export class EncuestaModule {}
