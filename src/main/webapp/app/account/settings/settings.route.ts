import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { SettingsComponent } from './settings.component';

export const settingsRoute: Route = {
    path: 'settings',
    component: SettingsComponent,
    data: {
        authorities: ['ROLE_USER', 'ROLE_BIG_USER', 'ROLE_HOME_USER', 'ROLE_ADMIN'],
        pageTitle: 'Settings'
    },
    canActivate: [UserRouteAccessService]
};
