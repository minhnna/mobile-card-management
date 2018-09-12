import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { CardMySuffixService } from './card-my-suffix.service';
import { DATA } from 'app/shared/constants/data.constants';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CardMySuffixChangeDialogComponent } from './card-my-suffix-change-dialog.component';

@Component({
    selector: 'jhi-card-my-suffix',
    templateUrl: './card-my-suffix.component.html'
})
export class CardMySuffixComponent implements OnInit, OnDestroy {
    currentAccount: any;
    cards: ICardMySuffix[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    selectionMobileService = '';
    selectionAmountOf = '';
    selectionValue = '';

    mobileServices = DATA.mobileServices;
    amountOf = DATA.amountOf;
    values = DATA.values;

    modalRef: NgbModalRef;

    constructor(
        private cardService: CardMySuffixService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.cardService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<ICardMySuffix[]>) => this.paginateCards(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/card-my-suffix'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/card-my-suffix',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        // this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        // this.registerChangeInCards();
        this.eventSubscriber = this.eventManager.subscribe('updateSuccess', response => this.popOutChangedCard(response.data));
    }

    popOutChangedCard(data) {
        let cardIndex;
        this.cards.forEach((card, index) => {
            if (card.id === data.id) {
                cardIndex = index;
            }
        });

        if (typeof cardIndex === 'number') {
            this.cards.splice(cardIndex, 1);
        }
    }

    ngOnDestroy() {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }

    trackId(index: number, item: ICardMySuffix) {
        return item.id;
    }

    registerChangeInCards() {
        this.eventSubscriber = this.eventManager.subscribe('cardListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    receiveCard() {
        const params = {
            mobileService: this.selectionMobileService.toUpperCase()
        };
        if (this.selectionValue) {
            params.price = this.selectionValue;
        }

        if (this.selectionAmountOf) {
            params.quantity = this.selectionAmountOf;
        }
        this.cardService
            .getCardByUser(params)
            .subscribe(
                (res: HttpResponse<ICardMySuffix[]>) => this.pushIntoListCard(res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    pushIntoListCard(data) {
        if (!this.cards) {
            this.cards = [];
        }

        if (data && data.length) {
            if (data.length === 1) {
                this.cards.push(data[0]);
            } else {
                data.forEach(card => {
                    this.cards.push(card);
                });
            }
        }
    }

    openChange(card) {
        this.modalRef = this.modalService.open(CardMySuffixChangeDialogComponent, { size: 'lg' });
        this.modalRef.componentInstance.card = card;
    }

    private paginateCards(data: ICardMySuffix[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.cards = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
