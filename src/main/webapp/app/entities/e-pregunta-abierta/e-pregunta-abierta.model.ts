import { IEPreguntaAbiertaRespuesta } from 'app/entities/e-pregunta-abierta-respuesta/e-pregunta-abierta-respuesta.model';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';

export interface IEPreguntaAbierta {
  id?: number;
  nombre?: string;
  opcional?: boolean;
  orden?: number;
  ePreguntaAbiertaRespuestas?: IEPreguntaAbiertaRespuesta[] | null;
  encuesta?: IEncuesta | null;
}

export class EPreguntaAbierta implements IEPreguntaAbierta {
  constructor(
    public id?: number,
    public nombre?: string,
    public opcional?: boolean,
    public orden?: number,
    public ePreguntaAbiertaRespuestas?: IEPreguntaAbiertaRespuesta[] | null,
    public encuesta?: IEncuesta | null
  ) {
    this.opcional = this.opcional ?? false;
  }
}

export function getEPreguntaAbiertaIdentifier(ePreguntaAbierta: IEPreguntaAbierta): number | undefined {
  return ePreguntaAbierta.id;
}
