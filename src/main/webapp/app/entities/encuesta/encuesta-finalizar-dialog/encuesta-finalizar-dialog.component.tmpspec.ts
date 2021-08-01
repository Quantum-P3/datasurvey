import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncuestaFinalizarDialogComponent } from './encuesta-finalizar-dialog.component';

describe('EncuestaFinalizarDialogComponent', () => {
  let component: EncuestaFinalizarDialogComponent;
  let fixture: ComponentFixture<EncuestaFinalizarDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EncuestaFinalizarDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EncuestaFinalizarDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
