import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { PAGINA_PRINCIPAL_ROUTE } from './pagina-princial.route';
import { PaginaPrincipalComponent } from './pagina-principal.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([PAGINA_PRINCIPAL_ROUTE])],
  declarations: [PaginaPrincipalComponent],
})
export class PaginaPrincipalModule {}
