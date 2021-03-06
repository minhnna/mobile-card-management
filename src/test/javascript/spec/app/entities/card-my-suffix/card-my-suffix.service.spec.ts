/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CardMySuffixService } from 'app/entities/card-my-suffix/card-my-suffix.service';
import { ICardMySuffix, CardMySuffix } from 'app/shared/model/card-my-suffix.model';

describe('Service Tests', () => {
    describe('CardMySuffix Service', () => {
        let injector: TestBed;
        let service: CardMySuffixService;
        let httpMock: HttpTestingController;
        let elemDefault: ICardMySuffix;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(CardMySuffixService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new CardMySuffix(
                'ID',
                'AAAAAAA',
                0,
                'AAAAAAA',
                'AAAAAAA',
                currentDate,
                currentDate,
                currentDate,
                'AAAAAAA',
                'AAAAAAA',
                0,
                false
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        exportedDate: currentDate.format(DATE_TIME_FORMAT),
                        updatedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find('123')
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a CardMySuffix', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        exportedDate: currentDate.format(DATE_TIME_FORMAT),
                        updatedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        createdDate: currentDate,
                        exportedDate: currentDate,
                        updatedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new CardMySuffix(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a CardMySuffix', async () => {
                const returnedFromService = Object.assign(
                    {
                        mobileService: 'BBBBBB',
                        price: 1,
                        serialNumber: 'BBBBBB',
                        code: 'BBBBBB',
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        exportedDate: currentDate.format(DATE_TIME_FORMAT),
                        updatedDate: currentDate.format(DATE_TIME_FORMAT),
                        userId: 'BBBBBB',
                        status: 'BBBBBB',
                        realPrice: 1,
                        deleted: true
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        createdDate: currentDate,
                        exportedDate: currentDate,
                        updatedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of CardMySuffix', async () => {
                const returnedFromService = Object.assign(
                    {
                        mobileService: 'BBBBBB',
                        price: 1,
                        serialNumber: 'BBBBBB',
                        code: 'BBBBBB',
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        exportedDate: currentDate.format(DATE_TIME_FORMAT),
                        updatedDate: currentDate.format(DATE_TIME_FORMAT),
                        userId: 'BBBBBB',
                        status: 'BBBBBB',
                        realPrice: 1,
                        deleted: true
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        createdDate: currentDate,
                        exportedDate: currentDate,
                        updatedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a CardMySuffix', async () => {
                const rxPromise = service.delete('123').subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
