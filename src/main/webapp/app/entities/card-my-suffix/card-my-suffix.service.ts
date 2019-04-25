import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';

type EntityResponseType = HttpResponse<ICardMySuffix>;
type EntityArrayResponseType = HttpResponse<ICardMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class CardMySuffixService {
    public resourceUrl = SERVER_API_URL + 'api/cards';

    constructor(protected http: HttpClient) {}

    create(card: ICardMySuffix): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(card);
        return this.http
            .post<ICardMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(card: ICardMySuffix): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(card);
        return this.http
            .put<ICardMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ICardMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICardMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(card: ICardMySuffix): ICardMySuffix {
        const copy: ICardMySuffix = Object.assign({}, card, {
            createdDate: card.createdDate != null && card.createdDate.isValid() ? card.createdDate.toJSON() : null,
            exportedDate: card.exportedDate != null && card.exportedDate.isValid() ? card.exportedDate.toJSON() : null,
            updatedDate: card.updatedDate != null && card.updatedDate.isValid() ? card.updatedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.exportedDate = res.body.exportedDate != null ? moment(res.body.exportedDate) : null;
            res.body.updatedDate = res.body.updatedDate != null ? moment(res.body.updatedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((card: ICardMySuffix) => {
                card.createdDate = card.createdDate != null ? moment(card.createdDate) : null;
                card.exportedDate = card.exportedDate != null ? moment(card.exportedDate) : null;
                card.updatedDate = card.updatedDate != null ? moment(card.updatedDate) : null;
            });
        }
        return res;
    }
}
