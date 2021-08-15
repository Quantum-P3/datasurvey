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
import { PlantillaPublishStoreDialogComponent } from './plantilla-publish-store-dialog/plantilla-publish-store-dialog.component';
import { PlantillaDeleteStoreDialogComponent } from './plantilla-delete-store-dialog/plantilla-delete-store-dialog.component';
import { PlantillaChangeStatusDialogComponent } from './plantilla-change-status-dialog/plantilla-change-status-dialog.component';

@NgModule({
  imports: [SharedModule, PlantillaRoutingModule, FontAwesomeModule],
  declarations: [
    PlantillaComponent,
    PlantillaDetailComponent,
    PlantillaUpdateComponent,
    PlantillaDeleteDialogComponent,
    PlantillaDeleteQuestionDialogComponent,
    PlantillaDeleteOptionDialogComponent,
    PlantillaPublishStoreDialogComponent,
    PlantillaDeleteStoreDialogComponent,
    PlantillaChangeStatusDialogComponent,
  ],
  entryComponents: [PlantillaDeleteDialogComponent],
})
export class PlantillaModule {}
