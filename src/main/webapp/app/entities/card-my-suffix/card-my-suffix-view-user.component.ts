import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { DATE_TIME_FORMAT, DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CardMySuffixService } from './card-my-suffix.service';
import { DATA } from 'app/shared/constants/data.constants';
import * as Moment from 'moment';

@Component({
    selector: 'jhi-card-my-suffix-view-user',
    templateUrl: './card-my-suffix-view-user.component.html'
})
export class CardMySuffixViewUserComponent implements OnInit, OnDestroy {
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

    mobileServices = DATA.mobileServices;
    amountOf = DATA.amountOf;
    values = DATA.values;
    statuses = DATA.statuses;
    from: any;
    to: any;
    selectionMobileService = '';
    selectionStatus = '';
    selectionValue = '';

    constructor(
        private cardService: CardMySuffixService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
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
        const params = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        };

        if (this.selectionMobileService) {
            params['mobileService'] = this.selectionMobileService.toUpperCase();
        }

        if (this.selectionValue) {
            params['price'] = this.selectionValue;
        }

        if (this.selectionStatus) {
            params['status'] = this.selectionStatus;
        }

        if (this.from) {
            params['fromDate'] = Moment(this.from).format(DATE_FORMAT);
        }

        if (this.to) {
            params['toDate'] = Moment(this.to).format(DATE_FORMAT);
        }

        this.cardService
            .queryByUser(params)
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
        this.router.navigate(['/card-by-user'], {
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
            '/card-by-user',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        // this.registerChangeInCards();
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

    export() {
        const fromDateString = Moment(this.from).format(DATE_FORMAT);
        const toDateString = Moment(this.to).format(DATE_FORMAT);
        const req = {
            fromDate: fromDateString,
            toDate: toDateString
        };

        this.cardService.export(req).subscribe(data => {
            const downloadURL = window.URL.createObjectURL(new Blob([data.body], { type: 'application/vnd.ms-excel' }));
            const link = document.createElement('a');
            link.href = downloadURL;
            link.download = this.currentAccount.login + '_' + req.fromDate + '_' + req.toDate + '.xlsx';
            link.click();
        });
    }

    search() {
        this.loadAll();
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
