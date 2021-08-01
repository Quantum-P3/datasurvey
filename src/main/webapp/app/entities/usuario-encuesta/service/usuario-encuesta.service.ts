import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsuarioEncuesta, getUsuarioEncuestaIdentifier } from '../usuario-encuesta.model';

export type EntityResponseType = HttpResponse<IUsuarioEncuesta>;
export type EntityArrayResponseType = HttpResponse<IUsuarioEncuesta[]>;

@Injectable({ providedIn: 'root' })
export class UsuarioEncuestaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuario-encuestas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(usuarioEncuesta: IUsuarioEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarioEncuesta);
    return this.http
      .post<IUsuarioEncuesta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(usuarioEncuesta: IUsuarioEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarioEncuesta);
    const url = `${this.resourceUrl}/${getUsuarioEncuestaIdentifier(usuarioEncuesta) as number}`;
    return this.http
      .put<IUsuarioEncuesta>(url, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(usuarioEncuesta: IUsuarioEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarioEncuesta);
    const url = `${this.resourceUrl}/${getUsuarioEncuestaIdentifier(usuarioEncuesta) as number}`;
    return this.http
      .patch<IUsuarioEncuesta>(url, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUsuarioEncuesta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findCollaborators(id: number): Observable<EntityResponseType> {
    return this.http
      .get<any>(`${this.resourceUrl}/encuesta/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUsuarioEncuesta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUsuarioEncuestaToCollectionIfMissing(
    usuarioEncuestaCollection: IUsuarioEncuesta[],
    ...usuarioEncuestasToCheck: (IUsuarioEncuesta | null | undefined)[]
  ): IUsuarioEncuesta[] {
    const usuarioEncuestas: IUsuarioEncuesta[] = usuarioEncuestasToCheck.filter(isPresent);
    if (usuarioEncuestas.length > 0) {
      const usuarioEncuestaCollectionIdentifiers = usuarioEncuestaCollection.map(
        usuarioEncuestaItem => getUsuarioEncuestaIdentifier(usuarioEncuestaItem)!
      );
      const usuarioEncuestasToAdd = usuarioEncuestas.filter(usuarioEncuestaItem => {
        const usuarioEncuestaIdentifier = getUsuarioEncuestaIdentifier(usuarioEncuestaItem);
        if (usuarioEncuestaIdentifier == null || usuarioEncuestaCollectionIdentifiers.includes(usuarioEncuestaIdentifier)) {
          return false;
        }
        usuarioEncuestaCollectionIdentifiers.push(usuarioEncuestaIdentifier);
        return true;
      });
      return [...usuarioEncuestasToAdd, ...usuarioEncuestaCollection];
    }
    return usuarioEncuestaCollection;
  }

  protected convertDateFromClient(usuarioEncuesta: IUsuarioEncuesta): IUsuarioEncuesta {
    return Object.assign({}, usuarioEncuesta, {
      fechaAgregado: usuarioEncuesta.fechaAgregado?.isValid() ? usuarioEncuesta.fechaAgregado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaAgregado = res.body.fechaAgregado ? dayjs(res.body.fechaAgregado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((usuarioEncuesta: IUsuarioEncuesta) => {
        usuarioEncuesta.fechaAgregado = usuarioEncuesta.fechaAgregado ? dayjs(usuarioEncuesta.fechaAgregado) : undefined;
      });
    }
    return res;
  }
}
