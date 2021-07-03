import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPPreguntaCerrada, getPPreguntaCerradaIdentifier } from '../p-pregunta-cerrada.model';

export type EntityResponseType = HttpResponse<IPPreguntaCerrada>;
export type EntityArrayResponseType = HttpResponse<IPPreguntaCerrada[]>;

@Injectable({ providedIn: 'root' })
export class PPreguntaCerradaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/p-pregunta-cerradas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pPreguntaCerrada: IPPreguntaCerrada): Observable<EntityResponseType> {
    return this.http.post<IPPreguntaCerrada>(this.resourceUrl, pPreguntaCerrada, { observe: 'response' });
  }

  update(pPreguntaCerrada: IPPreguntaCerrada): Observable<EntityResponseType> {
    return this.http.put<IPPreguntaCerrada>(
      `${this.resourceUrl}/${getPPreguntaCerradaIdentifier(pPreguntaCerrada) as number}`,
      pPreguntaCerrada,
      { observe: 'response' }
    );
  }

  partialUpdate(pPreguntaCerrada: IPPreguntaCerrada): Observable<EntityResponseType> {
    return this.http.patch<IPPreguntaCerrada>(
      `${this.resourceUrl}/${getPPreguntaCerradaIdentifier(pPreguntaCerrada) as number}`,
      pPreguntaCerrada,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPPreguntaCerrada>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPPreguntaCerrada[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPPreguntaCerradaToCollectionIfMissing(
    pPreguntaCerradaCollection: IPPreguntaCerrada[],
    ...pPreguntaCerradasToCheck: (IPPreguntaCerrada | null | undefined)[]
  ): IPPreguntaCerrada[] {
    const pPreguntaCerradas: IPPreguntaCerrada[] = pPreguntaCerradasToCheck.filter(isPresent);
    if (pPreguntaCerradas.length > 0) {
      const pPreguntaCerradaCollectionIdentifiers = pPreguntaCerradaCollection.map(
        pPreguntaCerradaItem => getPPreguntaCerradaIdentifier(pPreguntaCerradaItem)!
      );
      const pPreguntaCerradasToAdd = pPreguntaCerradas.filter(pPreguntaCerradaItem => {
        const pPreguntaCerradaIdentifier = getPPreguntaCerradaIdentifier(pPreguntaCerradaItem);
        if (pPreguntaCerradaIdentifier == null || pPreguntaCerradaCollectionIdentifiers.includes(pPreguntaCerradaIdentifier)) {
          return false;
        }
        pPreguntaCerradaCollectionIdentifiers.push(pPreguntaCerradaIdentifier);
        return true;
      });
      return [...pPreguntaCerradasToAdd, ...pPreguntaCerradaCollection];
    }
    return pPreguntaCerradaCollection;
  }
}
