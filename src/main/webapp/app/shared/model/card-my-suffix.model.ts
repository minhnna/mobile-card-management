import { Moment } from 'moment';

export interface ICardMySuffix {
    id?: string;
    mobileService?: string;
    price?: number;
    serialNumber?: string;
    code?: string;
    createdDate?: Moment;
    exportedDate?: Moment;
    updatedDate?: Moment;
    userId?: string;
    status?: string;
    realPrice?: number;
    deleted?: boolean;
}

export class CardMySuffix implements ICardMySuffix {
    constructor(
        public id?: string,
        public mobileService?: string,
        public price?: number,
        public serialNumber?: string,
        public code?: string,
        public createdDate?: Moment,
        public exportedDate?: Moment,
        public updatedDate?: Moment,
        public userId?: string,
        public status?: string,
        public realPrice?: number,
        public deleted?: boolean
    ) {
        this.deleted = this.deleted || false;
    }
}
