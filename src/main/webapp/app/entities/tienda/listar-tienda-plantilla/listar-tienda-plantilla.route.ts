import { Route, RouterModule } from '@angular/router';
import { ListarTiendaPlantillaComponent } from './listar-tienda-plantilla.component';

export const TIENDA_PLANTILLA_ROUTE: Route = {
  path: 'tienda-plantilla',
  component: ListarTiendaPlantillaComponent,
  data: {
    pageTitle: 'dataSurveyApp.tiendaPlantilla.title',
  },
};
