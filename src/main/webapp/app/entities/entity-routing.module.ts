import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'parametro-aplicacion',
        data: { pageTitle: 'dataSurveyApp.parametroAplicacion.home.title' },
        loadChildren: () => import('./parametro-aplicacion/parametro-aplicacion.module').then(m => m.ParametroAplicacionModule),
      },
      {
        path: 'usuario-extra',
        data: { pageTitle: 'dataSurveyApp.usuarioExtra.home.title' },
        loadChildren: () => import('./usuario-extra/usuario-extra.module').then(m => m.UsuarioExtraModule),
      },
      {
        path: 'encuesta',
        data: { pageTitle: 'dataSurveyApp.encuesta.home.title' },
        loadChildren: () => import('./encuesta/encuesta.module').then(m => m.EncuestaModule),
      },
      {
        path: 'e-pregunta-abierta',
        data: { pageTitle: 'dataSurveyApp.ePreguntaAbierta.home.title' },
        loadChildren: () => import('./e-pregunta-abierta/e-pregunta-abierta.module').then(m => m.EPreguntaAbiertaModule),
      },
      {
        path: 'e-pregunta-abierta-respuesta',
        data: { pageTitle: 'dataSurveyApp.ePreguntaAbiertaRespuesta.home.title' },
        loadChildren: () =>
          import('./e-pregunta-abierta-respuesta/e-pregunta-abierta-respuesta.module').then(m => m.EPreguntaAbiertaRespuestaModule),
      },
      {
        path: 'e-pregunta-cerrada',
        data: { pageTitle: 'dataSurveyApp.ePreguntaCerrada.home.title' },
        loadChildren: () => import('./e-pregunta-cerrada/e-pregunta-cerrada.module').then(m => m.EPreguntaCerradaModule),
      },
      {
        path: 'e-pregunta-cerrada-opcion',
        data: { pageTitle: 'dataSurveyApp.ePreguntaCerradaOpcion.home.title' },
        loadChildren: () =>
          import('./e-pregunta-cerrada-opcion/e-pregunta-cerrada-opcion.module').then(m => m.EPreguntaCerradaOpcionModule),
      },
      {
        path: 'colaboraciones',
        data: { pageTitle: 'dataSurveyApp.usuarioEncuesta.home.title' },
        loadChildren: () => import('./usuario-encuesta/usuario-encuesta.module').then(m => m.UsuarioEncuestaModule),
      },
      {
        path: 'categoria',
        data: { pageTitle: 'dataSurveyApp.categoria.home.title' },
        loadChildren: () => import('./categoria/categoria.module').then(m => m.CategoriaModule),
      },
      {
        path: 'factura',
        data: { pageTitle: 'dataSurveyApp.factura.home.title' },
        loadChildren: () => import('./factura/factura.module').then(m => m.FacturaModule),
      },
      {
        path: 'plantilla',
        data: { pageTitle: 'dataSurveyApp.plantilla.home.title' },
        loadChildren: () => import('./plantilla/plantilla.module').then(m => m.PlantillaModule),
      },
      {
        path: 'p-pregunta-abierta',
        data: { pageTitle: 'dataSurveyApp.pPreguntaAbierta.home.title' },
        loadChildren: () => import('./p-pregunta-abierta/p-pregunta-abierta.module').then(m => m.PPreguntaAbiertaModule),
      },
      {
        path: 'p-pregunta-cerrada',
        data: { pageTitle: 'dataSurveyApp.pPreguntaCerrada.home.title' },
        loadChildren: () => import('./p-pregunta-cerrada/p-pregunta-cerrada.module').then(m => m.PPreguntaCerradaModule),
      },
      {
        path: 'p-pregunta-cerrada-opcion',
        data: { pageTitle: 'dataSurveyApp.pPreguntaCerradaOpcion.home.title' },
        loadChildren: () =>
          import('./p-pregunta-cerrada-opcion/p-pregunta-cerrada-opcion.module').then(m => m.PPreguntaCerradaOpcionModule),
      },
      {
        path: 'mis-plantillas',
        data: { pageTitle: 'dataSurveyApp.usuarioExtra.plantillas.title' },
        loadChildren: () => import('./usuario-plantillas/usuario-plantillas.module').then(m => m.UsuarioPlantillasModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
