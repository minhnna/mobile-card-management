import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';
import { CardMySuffixService } from './card-my-suffix.service';

@Component({
    selector: 'jhi-card-my-suffix-update',
    templateUrl: './card-my-suffix-update.component.html'
})
export class CardMySuffixUpdateComponent implements OnInit {
    card: ICardMySuffix;
    isSaving: boolean;
    createdDate: string;
    exportedDate: string;
    updatedDate: string;

    constructor(protected cardService: CardMySuffixService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ card }) => {
            this.card = card;
            this.createdDate = this.card.createdDate != null ? this.card.createdDate.format(DATE_TIME_FORMAT) : null;
            this.exportedDate = this.card.exportedDate != null ? this.card.exportedDate.format(DATE_TIME_FORMAT) : null;
            this.updatedDate = this.card.updatedDate != null ? this.card.updatedDate.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.card.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.card.exportedDate = this.exportedDate != null ? moment(this.exportedDate, DATE_TIME_FORMAT) : null;
        this.card.updatedDate = this.updatedDate != null ? moment(this.updatedDate, DATE_TIME_FORMAT) : null;
        if (this.card.id !== undefined) {
            this.subscribeToSaveResponse(this.cardService.update(this.card));
        } else {
            this.subscribeToSaveResponse(this.cardService.create(this.card));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardMySuffix>>) {
        result.subscribe((res: HttpResponse<ICardMySuffix>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
