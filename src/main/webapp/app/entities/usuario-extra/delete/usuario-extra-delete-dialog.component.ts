import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioExtra } from '../usuario-extra.model';
import { UsuarioExtraService } from '../service/usuario-extra.service';

@Component({
  templateUrl: './usuario-extra-delete-dialog.component.html',
})
export class UsuarioExtraDeleteDialogComponent {
  usuarioExtra?: IUsuarioExtra;

  constructor(protected usuarioExtraService: UsuarioExtraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.usuarioExtraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
