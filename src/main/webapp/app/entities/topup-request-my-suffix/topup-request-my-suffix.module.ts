import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MobileCardManagementSharedModule } from 'app/shared';
import {
    TopupRequestMySuffixComponent,
    TopupRequestMySuffixDetailComponent,
    TopupRequestMySuffixUpdateComponent,
    TopupRequestMySuffixDeletePopupComponent,
    TopupRequestMySuffixDeleteDialogComponent,
    topupRequestRoute,
    topupRequestPopupRoute
} from './';

const ENTITY_STATES = [...topupRequestRoute, ...topupRequestPopupRoute];

@NgModule({
    imports: [MobileCardManagementSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TopupRequestMySuffixComponent,
        TopupRequestMySuffixDetailComponent,
        TopupRequestMySuffixUpdateComponent,
        TopupRequestMySuffixDeleteDialogComponent,
        TopupRequestMySuffixDeletePopupComponent
    ],
    entryComponents: [
        TopupRequestMySuffixComponent,
        TopupRequestMySuffixUpdateComponent,
        TopupRequestMySuffixDeleteDialogComponent,
        TopupRequestMySuffixDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MobileCardManagementTopupRequestMySuffixModule {}
