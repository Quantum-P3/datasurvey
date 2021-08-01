import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IEncuesta } from '../encuesta.model';

@Component({
  selector: 'jhi-encuesta-compartir-dialog',
  templateUrl: './encuesta-compartir-dialog.component.html',
  styleUrls: ['./encuesta-compartir-dialog.component.scss'],
})
export class EncuestaCompartirDialogComponent implements OnInit {
  encuesta?: IEncuesta;
  baseURL: string = '';
  imagen: string = '';
  name = 'ngx sharebuttons';

  constructor(protected activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    this.baseURL = location.origin + '/' + this.encuesta?.id + '/complete';
  }

  compartir(): void {}

  cancel(): void {
    this.activeModal.dismiss();
  }
}
