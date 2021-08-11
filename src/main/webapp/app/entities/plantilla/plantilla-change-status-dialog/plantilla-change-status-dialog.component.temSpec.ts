import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlantillaChangeStatusDialogComponent } from './plantilla-change-status-dialog.component';

describe('PlantillaChangeStatusDialogComponent', () => {
  let component: PlantillaChangeStatusDialogComponent;
  let fixture: ComponentFixture<PlantillaChangeStatusDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PlantillaChangeStatusDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlantillaChangeStatusDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
