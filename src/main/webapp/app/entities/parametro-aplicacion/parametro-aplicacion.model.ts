export interface IParametroAplicacion {
  id?: number;
  maxDiasEncuesta?: number;
  minDiasEncuesta?: number;
  maxCantidadPreguntas?: number;
  minCantidadPreguntas?: number;
}

export class ParametroAplicacion implements IParametroAplicacion {
  constructor(
    public id?: number,
    public maxDiasEncuesta?: number,
    public minDiasEncuesta?: number,
    public maxCantidadPreguntas?: number,
    public minCantidadPreguntas?: number
  ) {}
}

export function getParametroAplicacionIdentifier(parametroAplicacion: IParametroAplicacion): number | undefined {
  return parametroAplicacion.id;
}
