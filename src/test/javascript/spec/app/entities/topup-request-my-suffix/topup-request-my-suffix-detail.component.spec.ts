/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MobileCardManagementTestModule } from '../../../test.module';
import { TopupRequestMySuffixDetailComponent } from 'app/entities/topup-request-my-suffix/topup-request-my-suffix-detail.component';
import { TopupRequestMySuffix } from 'app/shared/model/topup-request-my-suffix.model';

describe('Component Tests', () => {
    describe('TopupRequestMySuffix Management Detail Component', () => {
        let comp: TopupRequestMySuffixDetailComponent;
        let fixture: ComponentFixture<TopupRequestMySuffixDetailComponent>;
        const route = ({ data: of({ topupRequest: new TopupRequestMySuffix('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MobileCardManagementTestModule],
                declarations: [TopupRequestMySuffixDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TopupRequestMySuffixDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TopupRequestMySuffixDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.topupRequest).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
