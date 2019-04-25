import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ITopupRequestMySuffix } from 'app/shared/model/topup-request-my-suffix.model';
import { TopupRequestMySuffixService } from './topup-request-my-suffix.service';

@Component({
    selector: 'jhi-topup-request-my-suffix-update',
    templateUrl: './topup-request-my-suffix-update.component.html'
})
export class TopupRequestMySuffixUpdateComponent implements OnInit {
    topupRequest: ITopupRequestMySuffix;
    isSaving: boolean;
    createdDate: string;
    updatedDate: string;

    constructor(protected topupRequestService: TopupRequestMySuffixService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ topupRequest }) => {
            this.topupRequest = topupRequest;
            this.createdDate = this.topupRequest.createdDate != null ? this.topupRequest.createdDate.format(DATE_TIME_FORMAT) : null;
            this.updatedDate = this.topupRequest.updatedDate != null ? this.topupRequest.updatedDate.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.topupRequest.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.topupRequest.updatedDate = this.updatedDate != null ? moment(this.updatedDate, DATE_TIME_FORMAT) : null;
        if (this.topupRequest.id !== undefined) {
            this.subscribeToSaveResponse(this.topupRequestService.update(this.topupRequest));
        } else {
            this.subscribeToSaveResponse(this.topupRequestService.create(this.topupRequest));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITopupRequestMySuffix>>) {
        result.subscribe(
            (res: HttpResponse<ITopupRequestMySuffix>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
