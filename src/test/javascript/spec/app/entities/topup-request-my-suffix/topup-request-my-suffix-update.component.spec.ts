/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MobileCardManagementTestModule } from '../../../test.module';
import { TopupRequestMySuffixUpdateComponent } from 'app/entities/topup-request-my-suffix/topup-request-my-suffix-update.component';
import { TopupRequestMySuffixService } from 'app/entities/topup-request-my-suffix/topup-request-my-suffix.service';
import { TopupRequestMySuffix } from 'app/shared/model/topup-request-my-suffix.model';

describe('Component Tests', () => {
    describe('TopupRequestMySuffix Management Update Component', () => {
        let comp: TopupRequestMySuffixUpdateComponent;
        let fixture: ComponentFixture<TopupRequestMySuffixUpdateComponent>;
        let service: TopupRequestMySuffixService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MobileCardManagementTestModule],
                declarations: [TopupRequestMySuffixUpdateComponent]
            })
                .overrideTemplate(TopupRequestMySuffixUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TopupRequestMySuffixUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TopupRequestMySuffixService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new TopupRequestMySuffix('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.topupRequest = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new TopupRequestMySuffix();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.topupRequest = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
