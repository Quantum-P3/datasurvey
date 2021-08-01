import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncuestaEncuestaCompleteComponent } from './complete.component';

describe('EncuestaEncuestaCompleteComponent', () => {
  let component: EncuestaEncuestaCompleteComponent;
  let fixture: ComponentFixture<EncuestaEncuestaCompleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EncuestaEncuestaCompleteComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EncuestaEncuestaCompleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
