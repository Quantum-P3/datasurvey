import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEPreguntaCerrada, getEPreguntaCerradaIdentifier } from '../e-pregunta-cerrada.model';

export type EntityResponseType = HttpResponse<IEPreguntaCerrada>;
export type EntityArrayResponseType = HttpResponse<IEPreguntaCerrada[]>;

@Injectable({ providedIn: 'root' })
export class EPreguntaCerradaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/e-pregunta-cerradas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ePreguntaCerrada: IEPreguntaCerrada): Observable<EntityResponseType> {
    return this.http.post<IEPreguntaCerrada>(this.resourceUrl, ePreguntaCerrada, { observe: 'response' });
  }

  update(ePreguntaCerrada: IEPreguntaCerrada): Observable<EntityResponseType> {
    return this.http.put<IEPreguntaCerrada>(
      `${this.resourceUrl}/${getEPreguntaCerradaIdentifier(ePreguntaCerrada) as number}`,
      ePreguntaCerrada,
      { observe: 'response' }
    );
  }

  partialUpdate(ePreguntaCerrada: IEPreguntaCerrada): Observable<EntityResponseType> {
    return this.http.patch<IEPreguntaCerrada>(
      `${this.resourceUrl}/${getEPreguntaCerradaIdentifier(ePreguntaCerrada) as number}`,
      ePreguntaCerrada,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEPreguntaCerrada>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEPreguntaCerrada[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEPreguntaCerradaToCollectionIfMissing(
    ePreguntaCerradaCollection: IEPreguntaCerrada[],
    ...ePreguntaCerradasToCheck: (IEPreguntaCerrada | null | undefined)[]
  ): IEPreguntaCerrada[] {
    const ePreguntaCerradas: IEPreguntaCerrada[] = ePreguntaCerradasToCheck.filter(isPresent);
    if (ePreguntaCerradas.length > 0) {
      const ePreguntaCerradaCollectionIdentifiers = ePreguntaCerradaCollection.map(
        ePreguntaCerradaItem => getEPreguntaCerradaIdentifier(ePreguntaCerradaItem)!
      );
      const ePreguntaCerradasToAdd = ePreguntaCerradas.filter(ePreguntaCerradaItem => {
        const ePreguntaCerradaIdentifier = getEPreguntaCerradaIdentifier(ePreguntaCerradaItem);
        if (ePreguntaCerradaIdentifier == null || ePreguntaCerradaCollectionIdentifiers.includes(ePreguntaCerradaIdentifier)) {
          return false;
        }
        ePreguntaCerradaCollectionIdentifiers.push(ePreguntaCerradaIdentifier);
        return true;
      });
      return [...ePreguntaCerradasToAdd, ...ePreguntaCerradaCollection];
    }
    return ePreguntaCerradaCollection;
  }
}
