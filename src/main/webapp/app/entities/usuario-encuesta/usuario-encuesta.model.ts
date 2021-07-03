import * as dayjs from 'dayjs';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { RolColaborador } from 'app/entities/enumerations/rol-colaborador.model';
import { EstadoColaborador } from 'app/entities/enumerations/estado-colaborador.model';

export interface IUsuarioEncuesta {
  id?: number;
  rol?: RolColaborador;
  estado?: EstadoColaborador;
  fechaAgregado?: dayjs.Dayjs;
  usuarioExtra?: IUsuarioExtra | null;
  encuesta?: IEncuesta | null;
}

export class UsuarioEncuesta implements IUsuarioEncuesta {
  constructor(
    public id?: number,
    public rol?: RolColaborador,
    public estado?: EstadoColaborador,
    public fechaAgregado?: dayjs.Dayjs,
    public usuarioExtra?: IUsuarioExtra | null,
    public encuesta?: IEncuesta | null
  ) {}
}

export function getUsuarioEncuestaIdentifier(usuarioEncuesta: IUsuarioEncuesta): number | undefined {
  return usuarioEncuesta.id;
}
