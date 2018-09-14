import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';
import { DATA } from 'app/shared/constants/data.constants';
import { CardMySuffixService } from './card-my-suffix.service';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

@Component({
    selector: 'jhi-card-my-suffix-change-dialog',
    templateUrl: './card-my-suffix-change-dialog.component.html'
})
export class CardMySuffixChangeDialogComponent implements OnInit {
    card: ICardMySuffix;
    values = DATA.values;
    updateCard: ICardMySuffix;
    isSaving = false;

    constructor(private cardService: CardMySuffixService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    ngOnInit() {
        this.updateCard = JSON.parse(JSON.stringify(this.card));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmChange() {
        this.isSaving = true;
        this.updateCard.createdDate = moment(this.updateCard.createdDate, DATE_TIME_FORMAT);
        this.updateCard.exportedDate = moment(this.updateCard.exportedDate, DATE_TIME_FORMAT);
        this.updateCard.updatedDate = moment(this.updateCard.updatedDate, DATE_TIME_FORMAT);
        this.cardService
            .update(this.updateCard)
            .subscribe((res: HttpResponse<ICardMySuffix>) => this.onSaveSuccess(res), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(res) {
        this.isSaving = false;
        this.eventManager.broadcast({
            name: 'updateSuccess',
            data: res.body
        });
        this.clear();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-card-my-suffix-change-popup',
    template: ''
})
export class CardMySuffixChangePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ card }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CardMySuffixChangeDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.card = card;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
