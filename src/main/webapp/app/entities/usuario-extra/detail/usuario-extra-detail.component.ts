import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUsuarioExtra } from '../usuario-extra.model';

@Component({
  selector: 'jhi-usuario-extra-detail',
  templateUrl: './usuario-extra-detail.component.html',
})
export class UsuarioExtraDetailComponent implements OnInit {
  usuarioExtra: IUsuarioExtra | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioExtra }) => {
      this.usuarioExtra = usuarioExtra;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
