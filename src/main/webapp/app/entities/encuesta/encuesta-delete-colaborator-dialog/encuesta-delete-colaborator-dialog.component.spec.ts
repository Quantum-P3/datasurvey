import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncuestaDeleteColaboratorDialogComponent } from './encuesta-delete-colaborator-dialog.component';

describe('EncuestaDeleteColaboratorDialogComponent', () => {
  let component: EncuestaDeleteColaboratorDialogComponent;
  let fixture: ComponentFixture<EncuestaDeleteColaboratorDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EncuestaDeleteColaboratorDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EncuestaDeleteColaboratorDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
