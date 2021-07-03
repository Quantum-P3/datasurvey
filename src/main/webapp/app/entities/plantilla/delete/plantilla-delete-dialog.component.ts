import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlantilla } from '../plantilla.model';
import { PlantillaService } from '../service/plantilla.service';

@Component({
  templateUrl: './plantilla-delete-dialog.component.html',
})
export class PlantillaDeleteDialogComponent {
  plantilla?: IPlantilla;

  constructor(protected plantillaService: PlantillaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plantillaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
