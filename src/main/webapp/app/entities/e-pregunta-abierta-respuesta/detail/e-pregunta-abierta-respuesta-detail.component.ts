import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEPreguntaAbiertaRespuesta } from '../e-pregunta-abierta-respuesta.model';

@Component({
  selector: 'jhi-e-pregunta-abierta-respuesta-detail',
  templateUrl: './e-pregunta-abierta-respuesta-detail.component.html',
})
export class EPreguntaAbiertaRespuestaDetailComponent implements OnInit {
  ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ePreguntaAbiertaRespuesta }) => {
      this.ePreguntaAbiertaRespuesta = ePreguntaAbiertaRespuesta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
