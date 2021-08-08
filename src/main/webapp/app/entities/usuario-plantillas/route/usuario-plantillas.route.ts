import { Route, RouterModule } from '@angular/router';

import { UsuarioPlantillasComponent } from '../list/usuario-plantillas.component';

export const USUARIO_PLANTILLAS_ROUTE: Route = {
  path: '',
  component: UsuarioPlantillasComponent,
  data: {
    pageTitle: 'dataSurveyApp.usuarioExtra.plantillas.title',
  },
};
