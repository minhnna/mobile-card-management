import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITopupRequestMySuffix } from 'app/shared/model/topup-request-my-suffix.model';

type EntityResponseType = HttpResponse<ITopupRequestMySuffix>;
type EntityArrayResponseType = HttpResponse<ITopupRequestMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class TopupRequestMySuffixService {
    public resourceUrl = SERVER_API_URL + 'api/topup-requests';

    constructor(protected http: HttpClient) {}

    create(topupRequest: ITopupRequestMySuffix): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(topupRequest);
        return this.http
            .post<ITopupRequestMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(topupRequest: ITopupRequestMySuffix): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(topupRequest);
        return this.http
            .put<ITopupRequestMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ITopupRequestMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITopupRequestMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(topupRequest: ITopupRequestMySuffix): ITopupRequestMySuffix {
        const copy: ITopupRequestMySuffix = Object.assign({}, topupRequest, {
            createdDate: topupRequest.createdDate != null && topupRequest.createdDate.isValid() ? topupRequest.createdDate.toJSON() : null,
            updatedDate: topupRequest.updatedDate != null && topupRequest.updatedDate.isValid() ? topupRequest.updatedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.updatedDate = res.body.updatedDate != null ? moment(res.body.updatedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((topupRequest: ITopupRequestMySuffix) => {
                topupRequest.createdDate = topupRequest.createdDate != null ? moment(topupRequest.createdDate) : null;
                topupRequest.updatedDate = topupRequest.updatedDate != null ? moment(topupRequest.updatedDate) : null;
            });
        }
        return res;
    }
}
