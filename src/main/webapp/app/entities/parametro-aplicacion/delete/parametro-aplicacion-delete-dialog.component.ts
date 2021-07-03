import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParametroAplicacion } from '../parametro-aplicacion.model';
import { ParametroAplicacionService } from '../service/parametro-aplicacion.service';

@Component({
  templateUrl: './parametro-aplicacion-delete-dialog.component.html',
})
export class ParametroAplicacionDeleteDialogComponent {
  parametroAplicacion?: IParametroAplicacion;

  constructor(protected parametroAplicacionService: ParametroAplicacionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parametroAplicacionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
