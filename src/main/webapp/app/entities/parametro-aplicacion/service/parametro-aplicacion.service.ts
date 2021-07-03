import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParametroAplicacion, getParametroAplicacionIdentifier } from '../parametro-aplicacion.model';

export type EntityResponseType = HttpResponse<IParametroAplicacion>;
export type EntityArrayResponseType = HttpResponse<IParametroAplicacion[]>;

@Injectable({ providedIn: 'root' })
export class ParametroAplicacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parametro-aplicacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(parametroAplicacion: IParametroAplicacion): Observable<EntityResponseType> {
    return this.http.post<IParametroAplicacion>(this.resourceUrl, parametroAplicacion, { observe: 'response' });
  }

  update(parametroAplicacion: IParametroAplicacion): Observable<EntityResponseType> {
    return this.http.put<IParametroAplicacion>(
      `${this.resourceUrl}/${getParametroAplicacionIdentifier(parametroAplicacion) as number}`,
      parametroAplicacion,
      { observe: 'response' }
    );
  }

  partialUpdate(parametroAplicacion: IParametroAplicacion): Observable<EntityResponseType> {
    return this.http.patch<IParametroAplicacion>(
      `${this.resourceUrl}/${getParametroAplicacionIdentifier(parametroAplicacion) as number}`,
      parametroAplicacion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParametroAplicacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParametroAplicacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParametroAplicacionToCollectionIfMissing(
    parametroAplicacionCollection: IParametroAplicacion[],
    ...parametroAplicacionsToCheck: (IParametroAplicacion | null | undefined)[]
  ): IParametroAplicacion[] {
    const parametroAplicacions: IParametroAplicacion[] = parametroAplicacionsToCheck.filter(isPresent);
    if (parametroAplicacions.length > 0) {
      const parametroAplicacionCollectionIdentifiers = parametroAplicacionCollection.map(
        parametroAplicacionItem => getParametroAplicacionIdentifier(parametroAplicacionItem)!
      );
      const parametroAplicacionsToAdd = parametroAplicacions.filter(parametroAplicacionItem => {
        const parametroAplicacionIdentifier = getParametroAplicacionIdentifier(parametroAplicacionItem);
        if (parametroAplicacionIdentifier == null || parametroAplicacionCollectionIdentifiers.includes(parametroAplicacionIdentifier)) {
          return false;
        }
        parametroAplicacionCollectionIdentifiers.push(parametroAplicacionIdentifier);
        return true;
      });
      return [...parametroAplicacionsToAdd, ...parametroAplicacionCollection];
    }
    return parametroAplicacionCollection;
  }
}
