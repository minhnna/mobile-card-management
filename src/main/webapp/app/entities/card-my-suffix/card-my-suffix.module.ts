import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MobileCardManagementSharedModule } from 'app/shared';
import {
    CardMySuffixComponent,
    CardMySuffixDetailComponent,
    CardMySuffixUpdateComponent,
    CardMySuffixDeletePopupComponent,
    CardMySuffixDeleteDialogComponent,
    CardMySuffixChangePopupComponent,
    CardMySuffixChangeDialogComponent,
    CardMySuffixViewUserComponent,
    CardMySuffixViewAdminComponent,
    CardMySuffixLeftComponent,
    cardRoute,
    cardPopupRoute
} from './';

const ENTITY_STATES = [...cardRoute, ...cardPopupRoute];

@NgModule({
    imports: [MobileCardManagementSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CardMySuffixComponent,
        CardMySuffixDetailComponent,
        CardMySuffixUpdateComponent,
        CardMySuffixDeleteDialogComponent,
        CardMySuffixDeletePopupComponent,
        CardMySuffixChangePopupComponent,
        CardMySuffixChangeDialogComponent,
        CardMySuffixViewAdminComponent,
        CardMySuffixLeftComponent,
        CardMySuffixViewUserComponent
    ],
    entryComponents: [
        CardMySuffixComponent,
        CardMySuffixUpdateComponent,
        CardMySuffixDeleteDialogComponent,
        CardMySuffixDeletePopupComponent,
        CardMySuffixChangePopupComponent,
        CardMySuffixChangeDialogComponent,
        CardMySuffixViewAdminComponent,
        CardMySuffixLeftComponent,
        CardMySuffixViewUserComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MobileCardManagementCardMySuffixModule {}
