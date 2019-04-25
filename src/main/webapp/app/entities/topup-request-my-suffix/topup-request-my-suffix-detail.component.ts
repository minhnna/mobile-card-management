import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITopupRequestMySuffix } from 'app/shared/model/topup-request-my-suffix.model';

@Component({
    selector: 'jhi-topup-request-my-suffix-detail',
    templateUrl: './topup-request-my-suffix-detail.component.html'
})
export class TopupRequestMySuffixDetailComponent implements OnInit {
    topupRequest: ITopupRequestMySuffix;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ topupRequest }) => {
            this.topupRequest = topupRequest;
        });
    }

    previousState() {
        window.history.back();
    }
}
