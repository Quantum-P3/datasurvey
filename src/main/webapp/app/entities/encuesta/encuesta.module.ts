import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EncuestaComponent } from './list/encuesta.component';
import { EncuestaDetailComponent } from './detail/encuesta-detail.component';
import { EncuestaUpdateComponent } from './update/encuesta-update.component';
import { EncuestaDeleteDialogComponent } from './delete/encuesta-delete-dialog.component';
import { EncuestaRoutingModule } from './route/encuesta-routing.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { EncuestaPublishDialogComponent } from './encuesta-publish-dialog/encuesta-publish-dialog.component';
import { EncuestaDeleteQuestionDialogComponent } from './encuesta-delete-question-dialog/encuesta-delete-question-dialog.component';
import { EncuestaDeleteOptionDialogComponent } from './encuesta-delete-option-dialog/encuesta-delete-option-dialog.component';

@NgModule({
  imports: [SharedModule, EncuestaRoutingModule, FontAwesomeModule],
  declarations: [
    EncuestaComponent,
    EncuestaDetailComponent,
    EncuestaUpdateComponent,
    EncuestaDeleteDialogComponent,
    EncuestaPublishDialogComponent,
    EncuestaDeleteQuestionDialogComponent,
    EncuestaDeleteOptionDialogComponent,
  ],
  entryComponents: [EncuestaDeleteDialogComponent],
})
export class EncuestaModule {}
