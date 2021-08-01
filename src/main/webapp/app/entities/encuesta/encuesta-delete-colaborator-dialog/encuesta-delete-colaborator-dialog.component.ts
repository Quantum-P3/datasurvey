import { Component, OnInit } from '@angular/core';

import { IUsuarioEncuesta } from '../../usuario-encuesta/usuario-encuesta.model';
import { UsuarioEncuestaService } from '../../usuario-encuesta/service/usuario-encuesta.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-encuesta-delete-colaborator-dialog',
  templateUrl: './encuesta-delete-colaborator-dialog.component.html',
  styleUrls: ['./encuesta-delete-colaborator-dialog.component.scss'],
})
export class EncuestaDeleteColaboratorDialogComponent {
  colaborador?: IUsuarioEncuesta;

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
