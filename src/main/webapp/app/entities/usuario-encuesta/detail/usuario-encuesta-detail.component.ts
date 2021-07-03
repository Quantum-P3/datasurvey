import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUsuarioEncuesta } from '../usuario-encuesta.model';

@Component({
  selector: 'jhi-usuario-encuesta-detail',
  templateUrl: './usuario-encuesta-detail.component.html',
})
export class UsuarioEncuestaDetailComponent implements OnInit {
  usuarioEncuesta: IUsuarioEncuesta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioEncuesta }) => {
      this.usuarioEncuesta = usuarioEncuesta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
