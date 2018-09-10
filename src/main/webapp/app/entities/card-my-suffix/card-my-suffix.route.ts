import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CardMySuffix } from 'app/shared/model/card-my-suffix.model';
import { CardMySuffixService } from './card-my-suffix.service';
import { CardMySuffixComponent } from './card-my-suffix.component';
import { CardMySuffixViewUserComponent } from './card-my-suffix-view-user.component';
import { CardMySuffixDetailComponent } from './card-my-suffix-detail.component';
import { CardMySuffixUpdateComponent } from './card-my-suffix-update.component';
import { CardMySuffixDeletePopupComponent } from './card-my-suffix-delete-dialog.component';
import { CardMySuffixChangePopupComponent } from './card-my-suffix-change-dialog.component';
import { ICardMySuffix } from 'app/shared/model/card-my-suffix.model';

@Injectable({ providedIn: 'root' })
export class CardMySuffixResolve implements Resolve<ICardMySuffix> {
    constructor(private service: CardMySuffixService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((card: HttpResponse<CardMySuffix>) => card.body));
        }
        return of(new CardMySuffix());
    }
}

export const cardRoute: Routes = [
    {
        path: 'card-my-suffix',
        component: CardMySuffixComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Cards'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'card-my-suffix/:id/view',
        component: CardMySuffixDetailComponent,
        resolve: {
            card: CardMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cards'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'card-my-suffix/new',
        component: CardMySuffixUpdateComponent,
        resolve: {
            card: CardMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cards'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'card-my-suffix/:id/edit',
        component: CardMySuffixUpdateComponent,
        resolve: {
            card: CardMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cards'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'card-by-user',
        component: CardMySuffixViewUserComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cards'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cardPopupRoute: Routes = [
    {
        path: 'card-my-suffix/:id/delete',
        component: CardMySuffixDeletePopupComponent,
        resolve: {
            card: CardMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cards'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'card-my-suffix/:id/change',
        component: CardMySuffixChangePopupComponent,
        resolve: {
            card: CardMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cards'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
