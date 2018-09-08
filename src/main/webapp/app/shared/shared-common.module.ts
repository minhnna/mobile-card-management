import { NgModule } from '@angular/core';

import { MobileCardManagementSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [MobileCardManagementSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [MobileCardManagementSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class MobileCardManagementSharedCommonModule {}
