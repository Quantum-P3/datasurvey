import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParametroAplicacion } from '../parametro-aplicacion.model';

@Component({
  selector: 'jhi-parametro-aplicacion-detail',
  templateUrl: './parametro-aplicacion-detail.component.html',
  styleUrls: ['./parametro-aplicacion-detail.component.scss'],
})
export class ParametroAplicacionDetailComponent implements OnInit {
  parametroAplicacion: IParametroAplicacion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parametroAplicacion }) => {
      this.parametroAplicacion = parametroAplicacion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
