import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITopupRequestMySuffix } from 'app/shared/model/topup-request-my-suffix.model';
import { TopupRequestMySuffixService } from './topup-request-my-suffix.service';

@Component({
    selector: 'jhi-topup-request-my-suffix-delete-dialog',
    templateUrl: './topup-request-my-suffix-delete-dialog.component.html'
})
export class TopupRequestMySuffixDeleteDialogComponent {
    topupRequest: ITopupRequestMySuffix;

    constructor(
        protected topupRequestService: TopupRequestMySuffixService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.topupRequestService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'topupRequestListModification',
                content: 'Deleted an topupRequest'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-topup-request-my-suffix-delete-popup',
    template: ''
})
export class TopupRequestMySuffixDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ topupRequest }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TopupRequestMySuffixDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.topupRequest = topupRequest;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/topup-request-my-suffix', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/topup-request-my-suffix', { outlets: { popup: null } }]);
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
