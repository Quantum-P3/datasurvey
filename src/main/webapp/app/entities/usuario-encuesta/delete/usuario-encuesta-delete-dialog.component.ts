import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioEncuesta } from '../usuario-encuesta.model';
import { UsuarioEncuestaService } from '../service/usuario-encuesta.service';

@Component({
  templateUrl: './usuario-encuesta-delete-dialog.component.html',
})
export class UsuarioEncuestaDeleteDialogComponent {
  usuarioEncuesta?: IUsuarioEncuesta;

  constructor(protected usuarioEncuestaService: UsuarioEncuestaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.usuarioEncuestaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
