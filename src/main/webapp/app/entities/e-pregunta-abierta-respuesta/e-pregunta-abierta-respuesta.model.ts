import { IEPreguntaAbierta } from 'app/entities/e-pregunta-abierta/e-pregunta-abierta.model';

export interface IEPreguntaAbiertaRespuesta {
  id?: number;
  respuesta?: string;
  epreguntaAbierta?: IEPreguntaAbierta | null;
}

export class EPreguntaAbiertaRespuesta implements IEPreguntaAbiertaRespuesta {
  constructor(public id?: number, public respuesta?: string, public epreguntaAbierta?: IEPreguntaAbierta | null) {}
}

export function getEPreguntaAbiertaRespuestaIdentifier(ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta): number | undefined {
  return ePreguntaAbiertaRespuesta.id;
}
