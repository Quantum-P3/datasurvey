import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SwiperlibComponent } from './swiperlib.component';

describe('SwiperlibComponent', () => {
  let component: SwiperlibComponent;
  let fixture: ComponentFixture<SwiperlibComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SwiperlibComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SwiperlibComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
