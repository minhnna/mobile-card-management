import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';
import { CardMySuffixService } from './card-my-suffix.service';

@Component({
    selector: 'jhi-card-my-suffix-update',
    templateUrl: './card-my-suffix-update.component.html'
})
export class CardMySuffixUpdateComponent implements OnInit {
    private _card: ICardMySuffix;
    isSaving: boolean;
    createdDate: string;
    exportedDate: string;
    updatedDate: string;

    constructor(private cardService: CardMySuffixService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ card }) => {
            this.card = card;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.card.createdDate = moment(this.createdDate, DATE_TIME_FORMAT);
        this.card.exportedDate = moment(this.exportedDate, DATE_TIME_FORMAT);
        this.card.updatedDate = moment(this.updatedDate, DATE_TIME_FORMAT);
        if (this.card.id !== undefined) {
            this.subscribeToSaveResponse(this.cardService.update(this.card));
        } else {
            this.subscribeToSaveResponse(this.cardService.create(this.card));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICardMySuffix>>) {
        result.subscribe((res: HttpResponse<ICardMySuffix>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get card() {
        return this._card;
    }

    set card(card: ICardMySuffix) {
        this._card = card;
        this.createdDate = moment(card.createdDate).format(DATE_TIME_FORMAT);
        this.exportedDate = moment(card.exportedDate).format(DATE_TIME_FORMAT);
        this.updatedDate = moment(card.updatedDate).format(DATE_TIME_FORMAT);
    }
}
