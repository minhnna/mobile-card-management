import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { PasswordComponent } from './password.component';

export const passwordRoute: Route = {
    path: 'password',
    component: PasswordComponent,
    data: {
        authorities: ['ROLE_USER', 'ROLE_BIG_USER', 'ROLE_HOME_USER', 'ROLE_ADMIN'],
        pageTitle: 'Password'
    },
    canActivate: [UserRouteAccessService]
};
