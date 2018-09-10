import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';
import { CardMySuffixService } from './card-my-suffix.service';

@Component({
    selector: 'jhi-card-my-suffix-change-dialog',
    templateUrl: './card-my-suffix-change-dialog.component.html'
})
export class CardMySuffixChangeDialogComponent {
    card: ICardMySuffix;

    constructor(private cardService: CardMySuffixService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmChange(id: string) {
        this.cardService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cardListModification',
                content: 'Changed an card'
            });
            this.activeModal.dismiss(true);
        });
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
