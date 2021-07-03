import * as dayjs from 'dayjs';
import { IUsuarioEncuesta } from 'app/entities/usuario-encuesta/usuario-encuesta.model';
import { IEPreguntaAbierta } from 'app/entities/e-pregunta-abierta/e-pregunta-abierta.model';
import { IEPreguntaCerrada } from 'app/entities/e-pregunta-cerrada/e-pregunta-cerrada.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { AccesoEncuesta } from 'app/entities/enumerations/acceso-encuesta.model';
import { EstadoEncuesta } from 'app/entities/enumerations/estado-encuesta.model';

export interface IEncuesta {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  fechaCreacion?: dayjs.Dayjs;
  fechaPublicacion?: dayjs.Dayjs | null;
  fechaFinalizar?: dayjs.Dayjs | null;
  fechaFinalizada?: dayjs.Dayjs | null;
  calificacion?: number;
  acceso?: AccesoEncuesta;
  contrasenna?: string | null;
  estado?: EstadoEncuesta;
  usuarioEncuestas?: IUsuarioEncuesta[] | null;
  ePreguntaAbiertas?: IEPreguntaAbierta[] | null;
  ePreguntaCerradas?: IEPreguntaCerrada[] | null;
  categoria?: ICategoria | null;
  usuarioExtra?: IUsuarioExtra | null;
}

export class Encuesta implements IEncuesta {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string | null,
    public fechaCreacion?: dayjs.Dayjs,
    public fechaPublicacion?: dayjs.Dayjs | null,
    public fechaFinalizar?: dayjs.Dayjs | null,
    public fechaFinalizada?: dayjs.Dayjs | null,
    public calificacion?: number,
    public acceso?: AccesoEncuesta,
    public contrasenna?: string | null,
    public estado?: EstadoEncuesta,
    public usuarioEncuestas?: IUsuarioEncuesta[] | null,
    public ePreguntaAbiertas?: IEPreguntaAbierta[] | null,
    public ePreguntaCerradas?: IEPreguntaCerrada[] | null,
    public categoria?: ICategoria | null,
    public usuarioExtra?: IUsuarioExtra | null
  ) {}
}

export function getEncuestaIdentifier(encuesta: IEncuesta): number | undefined {
  return encuesta.id;
}
