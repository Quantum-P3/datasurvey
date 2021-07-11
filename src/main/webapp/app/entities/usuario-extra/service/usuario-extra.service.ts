import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsuarioExtra, getUsuarioExtraIdentifier } from '../usuario-extra.model';
import { IUser } from '../../user/user.model';

export type EntityResponseType = HttpResponse<IUsuarioExtra>;
export type EntityArrayResponseType = HttpResponse<IUsuarioExtra[]>;

export type EntityArrayUserPublicResponseType = HttpResponse<IUser[]>;

@Injectable({ providedIn: 'root' })
export class UsuarioExtraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuario-extras');
  protected resourceUrlPublicUser = this.applicationConfigService.getEndpointFor('api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(usuarioExtra: IUsuarioExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarioExtra);
    return this.http
      .post<IUsuarioExtra>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(usuarioExtra: IUsuarioExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarioExtra);
    return this.http
      .put<IUsuarioExtra>(`${this.resourceUrl}/${getUsuarioExtraIdentifier(usuarioExtra) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(usuarioExtra: IUsuarioExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarioExtra);
    return this.http
      .patch<IUsuarioExtra>(`${this.resourceUrl}/${getUsuarioExtraIdentifier(usuarioExtra) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUsuarioExtra>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  retrieveAllPublicUsers(): Observable<IUser[]> {
    return this.http.get<IUser[]>(this.resourceUrlPublicUser + '/admin/users');
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUsuarioExtra[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUsuarioExtraToCollectionIfMissing(
    usuarioExtraCollection: IUsuarioExtra[],
    ...usuarioExtrasToCheck: (IUsuarioExtra | null | undefined)[]
  ): IUsuarioExtra[] {
    const usuarioExtras: IUsuarioExtra[] = usuarioExtrasToCheck.filter(isPresent);
    if (usuarioExtras.length > 0) {
      const usuarioExtraCollectionIdentifiers = usuarioExtraCollection.map(
        usuarioExtraItem => getUsuarioExtraIdentifier(usuarioExtraItem)!
      );
      const usuarioExtrasToAdd = usuarioExtras.filter(usuarioExtraItem => {
        const usuarioExtraIdentifier = getUsuarioExtraIdentifier(usuarioExtraItem);
        if (usuarioExtraIdentifier == null || usuarioExtraCollectionIdentifiers.includes(usuarioExtraIdentifier)) {
          return false;
        }
        usuarioExtraCollectionIdentifiers.push(usuarioExtraIdentifier);
        return true;
      });
      return [...usuarioExtrasToAdd, ...usuarioExtraCollection];
    }
    return usuarioExtraCollection;
  }

  protected convertDateFromClient(usuarioExtra: IUsuarioExtra): IUsuarioExtra {
    return Object.assign({}, usuarioExtra, {
      fechaNacimiento: usuarioExtra.fechaNacimiento?.isValid() ? usuarioExtra.fechaNacimiento.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaNacimiento = res.body.fechaNacimiento ? dayjs(res.body.fechaNacimiento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((usuarioExtra: IUsuarioExtra) => {
        usuarioExtra.fechaNacimiento = usuarioExtra.fechaNacimiento ? dayjs(usuarioExtra.fechaNacimiento) : undefined;
      });
    }
    return res;
  }
}
