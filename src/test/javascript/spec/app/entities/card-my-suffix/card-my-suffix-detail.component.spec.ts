/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MobileCardManagementTestModule } from '../../../test.module';
import { CardMySuffixDetailComponent } from 'app/entities/card-my-suffix/card-my-suffix-detail.component';
import { CardMySuffix } from 'app/shared/model/card-my-suffix.model';

describe('Component Tests', () => {
    describe('CardMySuffix Management Detail Component', () => {
        let comp: CardMySuffixDetailComponent;
        let fixture: ComponentFixture<CardMySuffixDetailComponent>;
        const route = ({ data: of({ card: new CardMySuffix('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MobileCardManagementTestModule],
                declarations: [CardMySuffixDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CardMySuffixDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CardMySuffixDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.card).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
