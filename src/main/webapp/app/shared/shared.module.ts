import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import {
    MobileCardManagementSharedLibsModule,
    MobileCardManagementSharedCommonModule,
    JhiLoginModalComponent,
    HasAnyAuthorityDirective
} from './';
import { MoneyFormatPipe } from './pipes/money-format.pipe';

@NgModule({
    imports: [MobileCardManagementSharedLibsModule, MobileCardManagementSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, MoneyFormatPipe],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [MobileCardManagementSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, MoneyFormatPipe],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MobileCardManagementSharedModule {}
