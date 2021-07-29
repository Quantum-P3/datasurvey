import { Route, RouterModule } from '@angular/router';

import { PaginaPrincipalComponent } from './pagina-principal.component';

export const PAGINA_PRINCIPAL_ROUTE: Route = {
  path: 'pagina-principal',
  component: PaginaPrincipalComponent,
  data: {
    pageTitle: 'paginaPrincipal.title',
  },
};
