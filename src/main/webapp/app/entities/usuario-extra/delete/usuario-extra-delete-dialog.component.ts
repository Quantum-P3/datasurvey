import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioExtra } from '../usuario-extra.model';
import { UsuarioExtraService } from '../service/usuario-extra.service';
import { EstadoUsuario } from '../../enumerations/estado-usuario.model';

@Component({
  templateUrl: './usuario-extra-delete-dialog.component.html',
})
export class UsuarioExtraDeleteDialogComponent {
  usuarioExtra?: IUsuarioExtra;

  constructor(protected usuarioExtraService: UsuarioExtraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(usuarioExtra: IUsuarioExtra): void {
    if (usuarioExtra.estado == EstadoUsuario.ACTIVE) {
      usuarioExtra.estado = EstadoUsuario.SUSPENDED;
    } else {
      usuarioExtra.estado = EstadoUsuario.ACTIVE;
    }
    this.usuarioExtraService.updateEstado(usuarioExtra).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
