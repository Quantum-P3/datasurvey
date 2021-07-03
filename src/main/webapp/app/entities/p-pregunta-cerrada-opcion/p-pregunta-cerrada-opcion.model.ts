import { IPPreguntaCerrada } from 'app/entities/p-pregunta-cerrada/p-pregunta-cerrada.model';

export interface IPPreguntaCerradaOpcion {
  id?: number;
  nombre?: string;
  orden?: number;
  pPreguntaCerrada?: IPPreguntaCerrada | null;
}

export class PPreguntaCerradaOpcion implements IPPreguntaCerradaOpcion {
  constructor(public id?: number, public nombre?: string, public orden?: number, public pPreguntaCerrada?: IPPreguntaCerrada | null) {}
}

export function getPPreguntaCerradaOpcionIdentifier(pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion): number | undefined {
  return pPreguntaCerradaOpcion.id;
}
