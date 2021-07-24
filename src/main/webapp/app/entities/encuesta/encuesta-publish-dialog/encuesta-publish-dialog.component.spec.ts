import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncuestaPublishDialogComponent } from './encuesta-publish-dialog.component';

describe('EncuestaPublishDialogComponent', () => {
  let component: EncuestaPublishDialogComponent;
  let fixture: ComponentFixture<EncuestaPublishDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EncuestaPublishDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EncuestaPublishDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
