import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncuestaCompartirDialogComponent } from './encuesta-compartir-dialog.component';

describe('EncuestaCompartirDialogComponent', () => {
  let component: EncuestaCompartirDialogComponent;
  let fixture: ComponentFixture<EncuestaCompartirDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EncuestaCompartirDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EncuestaCompartirDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
