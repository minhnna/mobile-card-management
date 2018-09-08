import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';

@Component({
    selector: 'jhi-card-my-suffix-detail',
    templateUrl: './card-my-suffix-detail.component.html'
})
export class CardMySuffixDetailComponent implements OnInit {
    card: ICardMySuffix;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ card }) => {
            this.card = card;
        });
    }

    previousState() {
        window.history.back();
    }
}
