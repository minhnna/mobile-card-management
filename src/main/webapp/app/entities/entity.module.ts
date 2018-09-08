import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MobileCardManagementCardMySuffixModule } from './card-my-suffix/card-my-suffix.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        MobileCardManagementCardMySuffixModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MobileCardManagementEntityModule {}
