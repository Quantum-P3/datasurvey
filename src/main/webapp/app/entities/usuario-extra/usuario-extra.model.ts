import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { IUsuarioEncuesta } from 'app/entities/usuario-encuesta/usuario-encuesta.model';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { EstadoUsuario } from 'app/entities/enumerations/estado-usuario.model';

export interface IUsuarioExtra {
  id?: number;
  nombre?: string;
  iconoPerfil?: number | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  estado?: EstadoUsuario;
  user?: IUser | null;
  encuestas?: IEncuesta[] | null;
  usuarioEncuestas?: IUsuarioEncuesta[] | null;
  plantillas?: IPlantilla[] | null;
}

export class UsuarioExtra implements IUsuarioExtra {
  constructor(
    public id?: number,
    public nombre?: string,
    public iconoPerfil?: number | null,
    public fechaNacimiento?: dayjs.Dayjs | null,
    public estado?: EstadoUsuario,
    public user?: IUser | null,
    public encuestas?: IEncuesta[] | null,
    public usuarioEncuestas?: IUsuarioEncuesta[] | null,
    public plantillas?: IPlantilla[] | null
  ) {}
}

export function getUsuarioExtraIdentifier(usuarioExtra: IUsuarioExtra): number | undefined {
  return usuarioExtra.id;
}
