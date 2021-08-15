import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncuestaCompleteComponent } from './complete.component';

describe('EncuestaCompleteComponent', () => {
  let component: EncuestaCompleteComponent;
  let fixture: ComponentFixture<EncuestaCompleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EncuestaCompleteComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EncuestaCompleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
