import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEPreguntaAbierta, getEPreguntaAbiertaIdentifier } from '../e-pregunta-abierta.model';

export type EntityResponseType = HttpResponse<IEPreguntaAbierta>;
export type EntityArrayResponseType = HttpResponse<IEPreguntaAbierta[]>;

@Injectable({ providedIn: 'root' })
export class EPreguntaAbiertaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/e-pregunta-abiertas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ePreguntaAbierta: IEPreguntaAbierta): Observable<EntityResponseType> {
    return this.http.post<IEPreguntaAbierta>(this.resourceUrl, ePreguntaAbierta, { observe: 'response' });
  }

  update(ePreguntaAbierta: IEPreguntaAbierta): Observable<EntityResponseType> {
    return this.http.put<IEPreguntaAbierta>(
      `${this.resourceUrl}/${getEPreguntaAbiertaIdentifier(ePreguntaAbierta) as number}`,
      ePreguntaAbierta,
      { observe: 'response' }
    );
  }

  partialUpdate(ePreguntaAbierta: IEPreguntaAbierta): Observable<EntityResponseType> {
    return this.http.patch<IEPreguntaAbierta>(
      `${this.resourceUrl}/${getEPreguntaAbiertaIdentifier(ePreguntaAbierta) as number}`,
      ePreguntaAbierta,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEPreguntaAbierta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEPreguntaAbierta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEPreguntaAbiertaToCollectionIfMissing(
    ePreguntaAbiertaCollection: IEPreguntaAbierta[],
    ...ePreguntaAbiertasToCheck: (IEPreguntaAbierta | null | undefined)[]
  ): IEPreguntaAbierta[] {
    const ePreguntaAbiertas: IEPreguntaAbierta[] = ePreguntaAbiertasToCheck.filter(isPresent);
    if (ePreguntaAbiertas.length > 0) {
      const ePreguntaAbiertaCollectionIdentifiers = ePreguntaAbiertaCollection.map(
        ePreguntaAbiertaItem => getEPreguntaAbiertaIdentifier(ePreguntaAbiertaItem)!
      );
      const ePreguntaAbiertasToAdd = ePreguntaAbiertas.filter(ePreguntaAbiertaItem => {
        const ePreguntaAbiertaIdentifier = getEPreguntaAbiertaIdentifier(ePreguntaAbiertaItem);
        if (ePreguntaAbiertaIdentifier == null || ePreguntaAbiertaCollectionIdentifiers.includes(ePreguntaAbiertaIdentifier)) {
          return false;
        }
        ePreguntaAbiertaCollectionIdentifiers.push(ePreguntaAbiertaIdentifier);
        return true;
      });
      return [...ePreguntaAbiertasToAdd, ...ePreguntaAbiertaCollection];
    }
    return ePreguntaAbiertaCollection;
  }
}
