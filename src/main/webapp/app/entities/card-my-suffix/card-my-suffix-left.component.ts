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
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'jhi-card-my-suffix-left',
    templateUrl: './card-my-suffix-left.component.html'
})
export class CardMySuffixLeftComponent implements OnInit, OnDestroy {
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

    setIntervalCheck: any;
    setIntervalPendingUsers: any;

    constructor(
        private cardService: CardMySuffixService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private toastrService: ToastrService
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
        if (this.principal.isRoleHomeUser()) {
            this.cardService
                .getExpiredCard({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<ICardMySuffix[]>) => this.paginateCards(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/card-by-admin'], {
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
            '/card-by-admin',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.getPendingUsers();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        // this.registerChangeInCards();

        this.setIntervalCheck = setInterval(() => {
            this.loadAll();
            this.getPendingUsers();
        }, 60000);
    }

    ngOnDestroy() {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
        clearInterval(this.setIntervalCheck);
    }

    getPendingUsers() {
        this.cardService.getPendingUser().subscribe(
            res => {
                if (res.body && res.body.length) {
                    let noti = '';
                    res.body.forEach((data, index) => {
                        noti = noti + data.login + ' - ' + data.email + (index > 0 ? ',' : '');
                    });
                    this.toastrService.info(noti);
                    this.playSoundNoti();
                }
            },
            err => {
                this.onError(err.message);
            }
        );
    }

    playSoundNoti() {
        const audio = new Audio();
        audio.src = '../../../../../content/audios/Cool-alarm-tone-notification-sound.mp3';
        audio.load();
        audio.play();
        setTimeout(() => {
            audio.pause();
        }, 4000);
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
        console.log(this.selectionMobileService, this.selectionAmountOf, this.selectionValue);
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
