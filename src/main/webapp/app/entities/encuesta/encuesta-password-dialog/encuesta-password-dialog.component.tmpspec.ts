import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncuestaPasswordDialogComponent } from './encuesta-password-dialog.component';

describe('EncuestaPasswordDialogComponent', () => {
  let component: EncuestaPasswordDialogComponent;
  let fixture: ComponentFixture<EncuestaPasswordDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EncuestaPasswordDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EncuestaPasswordDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
