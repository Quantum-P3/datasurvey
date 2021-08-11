import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarTiendaPlantillaComponent } from './listar-tienda-plantilla.component';

describe('ListarTiendaPlantillaComponent', () => {
  let component: ListarTiendaPlantillaComponent;
  let fixture: ComponentFixture<ListarTiendaPlantillaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListarTiendaPlantillaComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListarTiendaPlantillaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
