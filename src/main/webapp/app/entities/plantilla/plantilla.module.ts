import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlantillaComponent } from './list/plantilla.component';
import { PlantillaDetailComponent } from './detail/plantilla-detail.component';
import { PlantillaUpdateComponent } from './update/plantilla-update.component';
import { PlantillaDeleteDialogComponent } from './delete/plantilla-delete-dialog.component';
import { PlantillaRoutingModule } from './route/plantilla-routing.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { PlantillaDeleteQuestionDialogComponent } from './plantilla-delete-question-dialog/plantilla-delete-question-dialog.component';
import { PlantillaDeleteOptionDialogComponent } from './plantilla-delete-option-dialog/plantilla-delete-option-dialog.component';

@NgModule({
  imports: [SharedModule, PlantillaRoutingModule, FontAwesomeModule],
  declarations: [
    PlantillaComponent,
    PlantillaDetailComponent,
    PlantillaUpdateComponent,
    PlantillaDeleteDialogComponent,
    PlantillaDeleteQuestionDialogComponent,
    PlantillaDeleteOptionDialogComponent,
  ],
  entryComponents: [PlantillaDeleteDialogComponent],
})
export class PlantillaModule {}
