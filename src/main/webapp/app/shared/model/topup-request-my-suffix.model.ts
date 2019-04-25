import { Moment } from 'moment';

export interface ITopupRequestMySuffix {
    id?: string;
    mobileService?: string;
    mobileNumber?: string;
    topupValue?: number;
    realValue?: number;
    createdDate?: Moment;
    updatedDate?: Moment;
    userId?: string;
    status?: string;
    deleted?: boolean;
}

export class TopupRequestMySuffix implements ITopupRequestMySuffix {
    constructor(
        public id?: string,
        public mobileService?: string,
        public mobileNumber?: string,
        public topupValue?: number,
        public realValue?: number,
        public createdDate?: Moment,
        public updatedDate?: Moment,
        public userId?: string,
        public status?: string,
        public deleted?: boolean
    ) {
        this.deleted = this.deleted || false;
    }
}
