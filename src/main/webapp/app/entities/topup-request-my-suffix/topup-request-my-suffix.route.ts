import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TopupRequestMySuffix } from 'app/shared/model/topup-request-my-suffix.model';
import { TopupRequestMySuffixService } from './topup-request-my-suffix.service';
import { TopupRequestMySuffixComponent } from './topup-request-my-suffix.component';
import { TopupRequestMySuffixDetailComponent } from './topup-request-my-suffix-detail.component';
import { TopupRequestMySuffixUpdateComponent } from './topup-request-my-suffix-update.component';
import { TopupRequestMySuffixDeletePopupComponent } from './topup-request-my-suffix-delete-dialog.component';
import { ITopupRequestMySuffix } from 'app/shared/model/topup-request-my-suffix.model';

@Injectable({ providedIn: 'root' })
export class TopupRequestMySuffixResolve implements Resolve<ITopupRequestMySuffix> {
    constructor(private service: TopupRequestMySuffixService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITopupRequestMySuffix> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<TopupRequestMySuffix>) => response.ok),
                map((topupRequest: HttpResponse<TopupRequestMySuffix>) => topupRequest.body)
            );
        }
        return of(new TopupRequestMySuffix());
    }
}

export const topupRequestRoute: Routes = [
    {
        path: '',
        component: TopupRequestMySuffixComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'TopupRequests'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: TopupRequestMySuffixDetailComponent,
        resolve: {
            topupRequest: TopupRequestMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TopupRequests'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: TopupRequestMySuffixUpdateComponent,
        resolve: {
            topupRequest: TopupRequestMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TopupRequests'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: TopupRequestMySuffixUpdateComponent,
        resolve: {
            topupRequest: TopupRequestMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TopupRequests'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const topupRequestPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: TopupRequestMySuffixDeletePopupComponent,
        resolve: {
            topupRequest: TopupRequestMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TopupRequests'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
