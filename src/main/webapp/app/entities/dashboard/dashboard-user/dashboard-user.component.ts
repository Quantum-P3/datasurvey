import { Component, OnInit } from '@angular/core';
import { UsuarioEncuestaService } from '../../usuario-encuesta/service/usuario-encuesta.service';
import { IUser } from '../../user/user.model';
import { HttpResponse } from '@angular/common/http';
import { IEncuesta } from '../../encuesta/encuesta.model';
import { EstadoEncuesta } from '../../enumerations/estado-encuesta.model';
import { EncuestaService } from '../../encuesta/service/encuesta.service';
import { UsuarioExtra } from '../../usuario-extra/usuario-extra.model';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';
import { UsuarioExtraService } from '../../usuario-extra/service/usuario-extra.service';
import { faListAlt, faUser, faEye, faStar, faCalendarAlt } from '@fortawesome/free-solid-svg-icons';
import * as Chartist from 'chartist';
import { finalize } from 'rxjs/operators';
import { EPreguntaAbiertaRespuestaService } from '../../e-pregunta-abierta-respuesta/service/e-pregunta-abierta-respuesta.service';
import { IEPreguntaAbiertaRespuesta } from '../../e-pregunta-abierta-respuesta/e-pregunta-abierta-respuesta.model';
import { IUsuarioEncuesta } from '../../usuario-encuesta/usuario-encuesta.model';
import { exportAsExcelFile } from '../export/export_excel';
import { jsPDF } from 'jspdf';
import { createPDFTableHeaders, generatePDFTable, generatePDFTableData, saveGeneratedPDF } from '../export/export_pdf';

@Component({
  selector: 'jhi-dashboard-user',
  templateUrl: './dashboard-user.component.html',
  styleUrls: ['./dashboard-user.component.scss'],
})
export class DashboardUserComponent implements OnInit {
  user: IUser | null = null;
  cantEncuestas: number = 0;
  cantPersonas: number = 0;
  cantActivas: number = 0;
  cantFinalizadas: number = 0;
  cantDraft: number = 0;
  cantPublicas: number = 0;
  cantPrivadas: number = 0;
  faListAlt = faListAlt;
  faUser = faUser;
  faEye = faEye;
  faStar = faStar;
  faCalendarAlt = faCalendarAlt;
  reportsGeneral = false;
  reportForEncuestas = true;
  reportPreguntas = true;
  reportColaboraciones = true;
  reportColaboracionPreguntas = true;
  duracion?: number = 0;
  ePreguntas?: any[];
  ePreguntasOpciones?: any[];
  respuestaAbierta?: IEPreguntaAbiertaRespuesta[];
  isLoading = false;
  encuestas?: IEncuesta[];
  usuarioExtra: UsuarioExtra | null = null;
  account: Account | null = null;
  encuesta: IEncuesta | null = null;
  colaboracion: IEncuesta | null = null;
  preguntaId?: number = 0;
  usuarioEncuestas?: IUsuarioEncuesta[];
  colaboraciones?: IEncuesta[];
  duracionColaboracion?: number = 0;
  ePreguntasColaboracion?: any[];
  ePreguntasOpcionesColaboracion?: any[];
  respuestaAbiertaColaboracion?: IEPreguntaAbiertaRespuesta[];
  preguntaIdColaboracion?: number = 0;
  duracionArray?: number[] | null = null;

  constructor(
    protected encuestaService: EncuestaService,
    protected accountService: AccountService,
    protected usuarioExtraService: UsuarioExtraService,
    protected usuarioEncuestaService: UsuarioEncuestaService,
    protected resAbierta: EPreguntaAbiertaRespuestaService
  ) {}

  ngOnInit(): void {
    this.loadUser();
  }

  cambiarVista() {
    if (this.reportsGeneral) {
      this.reportsGeneral = false;
      this.reportForEncuestas = true;
      this.reportPreguntas = true;
      this.reportColaboraciones = true;
      this.reportColaboracionPreguntas = true;
    } else if (this.reportForEncuestas) {
      this.reportsGeneral = true;
      this.reportForEncuestas = false;
      this.reportPreguntas = true;
      this.reportColaboraciones = true;
      this.reportColaboracionPreguntas = true;
    } else if (this.reportPreguntas) {
      this.reportForEncuestas = false;
      this.reportPreguntas = true;
      this.reportsGeneral = true;
      this.reportColaboraciones = true;
      this.reportColaboracionPreguntas = true;
    }
  }

  cambiarVistaColaboracion(cambio: string) {
    if (cambio === 'colaboracion') {
      this.reportForEncuestas = true;
      this.reportPreguntas = true;
      this.reportsGeneral = true;
      this.reportColaboraciones = false;
      this.reportColaboracionPreguntas = true;
    } else if (cambio === 'preguntasColaboracion') {
      this.reportForEncuestas = true;
      this.reportPreguntas = true;
      this.reportsGeneral = true;
      this.reportColaboraciones = true;
      this.reportColaboracionPreguntas = false;
    }
  }

  loadEncuestas() {
    this.encuestaService.query().subscribe(
      (res: HttpResponse<IEncuesta[]>) => {
        this.isLoading = false;
        const tmpEncuestas = res.body ?? [];

        this.encuestas = tmpEncuestas.filter(e => e.usuarioExtra?.id === this.usuarioExtra?.id && e.estado !== 'DELETED');
        this.cantEncuestas = this.encuestas.length;
        this.cantActivas = tmpEncuestas.filter(e => e.estado === 'ACTIVE' && e.usuarioExtra?.id === this.usuarioExtra?.id).length;
        this.cantDraft = tmpEncuestas.filter(e => e.estado === 'DRAFT' && e.usuarioExtra?.id === this.usuarioExtra?.id).length;
        this.cantFinalizadas = tmpEncuestas.filter(e => e.estado === 'FINISHED' && e.usuarioExtra?.id === this.usuarioExtra?.id).length;
        this.cantPublicas = tmpEncuestas.filter(
          e => e.acceso === 'PUBLIC' && e.usuarioExtra?.id === this.usuarioExtra?.id && e.estado !== 'DELETED'
        ).length;
        this.cantPrivadas = tmpEncuestas.filter(
          e => e.acceso === 'PRIVATE' && e.usuarioExtra?.id === this.usuarioExtra?.id && e.estado !== 'DELETED'
        ).length;
        let cantidadCompletadas: number = 0;
        tmpEncuestas
          .filter(e => e.estado === 'ACTIVE')
          .forEach(e => {
            const _contadorCompletadas = e.calificacion;
            cantidadCompletadas = cantidadCompletadas + (Number(_contadorCompletadas?.toString().split('.')[1]) - 1);
          });
        this.cantPersonas = cantidadCompletadas;

        tmpEncuestas.forEach(encuesta => {
          const _calificacion = encuesta.calificacion;
          encuesta.calificacion = Number(_calificacion?.toString().split('.')[0]);

          /* if (encuesta.fechaFinalizada == null) {
            this.duracion = -1;
            this.duracionArray?.push(this.duracion);
          } else {
            this.duracion = encuesta.fechaPublicacion?.diff(encuesta.fechaFinalizada!, 'days');
            this.duracionArray?.push(this.duracion!);
          }*/
        });

        /*this.cantPersonas = tmpEncuestas.filter(
          e => e.calificacion && e.usuarioExtra?.id === this.usuarioExtra?.id && e.estado !== 'DELETED'
        ).length;*/
        //cantidad de personas que han completado la encuesta

        this.loadFirstChart();
        this.loadSecondChart();
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  loadUser(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account !== null) {
        this.usuarioExtraService.find(account.id).subscribe(usuarioExtra => {
          this.usuarioExtra = usuarioExtra.body;
        });
      }
    });

    this.loadEncuestas();
    this.loadAllColaboraciones();
  }

  loadFirstChart(): void {
    var dataEstado = {
      labels: ['ACTIVOS', 'BORRADOR', 'FINALIZADOS'],
      series: [this.cantActivas, this.cantDraft, this.cantFinalizadas],
    };

    new Chartist.Pie('#chartEstado', dataEstado);
  }

  loadSecondChart(): void {
    var dataAcceso = {
      labels: ['P??BLICA', 'PRIVADA'],
      series: [this.cantPublicas, this.cantPrivadas],
    };

    new Chartist.Pie('#chartAcceso', dataAcceso);
  }

  detallesPreguntas(encuesta: IEncuesta): void {
    if (!this.reportForEncuestas) {
      this.reportPreguntas = false;
      this.reportForEncuestas = true;
      this.reportsGeneral = true;
    }

    this.encuesta = encuesta;
    debugger;
    this.isLoading = true;
    this.encuestaService
      .findQuestions(encuesta?.id!)
      .pipe(
        finalize(() =>
          this.encuestaService.findQuestionsOptions(encuesta?.id!).subscribe(
            (res: any) => {
              this.isLoading = false;
              this.ePreguntasOpciones = res.body ?? [];

              debugger;

              this.getOpenQuestionAnswers();
            },
            () => {
              this.isLoading = false;
            }
          )
        )
      )
      .subscribe(
        (res: any) => {
          this.isLoading = false;
          this.ePreguntas = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    if (this.ePreguntas!.length == 0) {
      this.previousState();
    }
  }

  previousState(): void {
    window.history.back();
  }

  getOpenQuestionAnswers() {
    this.ePreguntas!.forEach(pregunta => {
      debugger;
      if (!pregunta.tipo) {
        this.resAbierta.query().subscribe(res => {
          debugger;

          this.preguntaId = pregunta.id;

          this.respuestaAbierta = res.body ?? [];
          /* const respuesta = res.body ?? [];

          respuesta.forEach( e => {
            debugger


            if (e.epreguntaAbierta?.id == pregunta.id){
              this.respuestaAbierta?.push(e);
            }
            /!*debugger
            this.eRespuestaAbierta?.push(respuesta.filter(e.ePreguntaAbierta?.id == pregunta.id));*!/
          })
*/
          console.log(this.respuestaAbierta);
        });
      }
    });
  }

  loadAllColaboraciones(): void {
    this.usuarioEncuestaService.query().subscribe((res: HttpResponse<IUsuarioEncuesta[]>) => {
      const tempUsuarioEncuestas = res.body ?? [];
      this.usuarioEncuestas = tempUsuarioEncuestas
        .filter(c => c.usuarioExtra?.id === this.usuarioExtra?.id)
        .filter(c => c.encuesta?.estado !== 'DELETED');

      // Fix calificacion
      tempUsuarioEncuestas.forEach(colaboracion => {
        if (colaboracion.encuesta) {
          const _calificacion = colaboracion.encuesta.calificacion;
          colaboracion.encuesta.calificacion = Number(_calificacion?.toString().split('.')[0]);

          if (colaboracion.encuesta.fechaFinalizada == null) {
            this.duracionColaboracion = -1;
          } else {
            this.duracionColaboracion = colaboracion.encuesta.fechaPublicacion?.diff(colaboracion.encuesta.fechaFinalizada!, 'days');
          }
        }
      });
    });
  }

  detallesPreguntasColaboracion(encuesta: IEncuesta): void {
    if (!this.reportColaboraciones) {
      this.reportPreguntas = true;
      this.reportForEncuestas = true;
      this.reportsGeneral = true;
      this.reportColaboraciones = true;
      this.reportColaboracionPreguntas = false;
    }

    this.colaboracion = encuesta;
    debugger;
    this.isLoading = true;
    this.encuestaService
      .findQuestions(encuesta?.id!)
      .pipe(
        finalize(() =>
          this.encuestaService.findQuestionsOptions(encuesta?.id!).subscribe(
            (res: any) => {
              this.isLoading = false;
              this.ePreguntasOpcionesColaboracion = res.body ?? [];

              //debugger;

              this.getOpenQuestionAnswersColaboracion();
            },
            () => {
              this.isLoading = false;
            }
          )
        )
      )
      .subscribe(
        (res: any) => {
          this.isLoading = false;
          this.ePreguntasColaboracion = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  getOpenQuestionAnswersColaboracion() {
    this.ePreguntasColaboracion!.forEach(pregunta => {
      debugger;
      if (!pregunta.tipo) {
        this.resAbierta.query().subscribe(res => {
          debugger;

          this.preguntaIdColaboracion = pregunta.id;

          this.respuestaAbiertaColaboracion = res.body ?? [];
          /* const respuesta = res.body ?? [];

          respuesta.forEach( e => {
            debugger


            if (e.epreguntaAbierta?.id == pregunta.id){
              this.respuestaAbierta?.push(e);
            }
            /!*debugger
            this.eRespuestaAbierta?.push(respuesta.filter(e.ePreguntaAbierta?.id == pregunta.id));*!/
          })
*/
          console.log(this.respuestaAbiertaColaboracion);
        });
      }
    });
  }

  exportReportesGeneralesUserExcel(): void {
    /*REPORTES GENERALES:
     *Cantidad de encuestas creadas
     * cantidad de personas que han completado las encuestas
     *Cantidad de encuestas por estado
     * Cantidad de encuestas por acceso
     * */

    if (!this.reportsGeneral) {
      const _sheets = [
        'Cantidad encuestas creadas',
        'Cantidad usuarios encuestas',
        'Cantidad encuestas por estado',
        'Cantidad encuestas por acceso',
      ];

      const _reporteEncuestasCreadas = [{ total_encuestas: this.cantEncuestas }];

      const _reporteUsuariosCompletadas = [{ total_usuarios: this.cantPersonas }];

      const _reporteEncuestasEstado = [
        { total_borrador: this.cantPersonas, total_activas: this.cantActivas, total_finalizadas: this.cantFinalizadas },
      ];

      const _reporteEncuestasAcceso = [{ total_publicas: this.cantPublicas, total_privadas: this.cantPrivadas }];

      const _excelFinalData = [_reporteEncuestasCreadas, _reporteUsuariosCompletadas, _reporteEncuestasEstado, _reporteEncuestasAcceso];
      const _fileName = 'reportes_generales_encuestas_DataSurvey';
      exportAsExcelFile(_sheets, _excelFinalData, _fileName);
    } else if (!this.reportPreguntas) {
      /*REPORTES POR ENCUESTA:
       * Nombre encuesta
       * categoria encuesta
       * calificacion encuesta
       *
       * preguntas:
       * contenido/ cantidad*/

      const _sheets = ['Datos de encuesta', 'Contenido de preguntas cerradas', 'Contenido de preguntas abiertas'];

      const _reporteDatosEncuesta = [
        {
          nombre_encuesta: this.encuesta?.nombre,
          categoria_encuesta: this.encuesta?.categoria?.nombre,
          calificacion_encuesta: this.encuesta?.calificacion,
        },
      ];

      const _reporteContenidoPreguntasCerradas: any[] = [];
      const _reporteContenidoPreguntasAbiertas: any[] = [];

      this.ePreguntas!.forEach((pregunta: any, index) => {
        debugger;
        let _report: any = {};
        let _reportAbierta: any = {};

        if (!pregunta.tipo) {
          this.respuestaAbierta!.forEach((respuesta: any) => {
            if (respuesta.epreguntaAbierta?.id == pregunta.id) {
              // _reportAbierta['pregunta_abierta'] = pregunta.nombre;
              _reportAbierta['contenido'] = respuesta.respuesta;
              _reporteContenidoPreguntasAbiertas.push(_reportAbierta);
            }
          });
        } else {
          debugger;

          this.ePreguntasOpciones!.forEach((respuesta: any, index) => {
            console.log(respuesta.epreguntaCerrada);

            if (respuesta[index].epreguntaCerrada.id == pregunta.id) {
              _report['opcion_pregunta'] = respuesta[index].nombre;
              _report['cantidad'] = respuesta[index].cantidad;
              _reporteContenidoPreguntasCerradas.push(_report);
            }
          });
        }
      });

      const _excelFinalData = [_reporteDatosEncuesta, _reporteContenidoPreguntasCerradas, _reporteContenidoPreguntasAbiertas];
      const _fileName = 'reportes_detalle_encuesta_datasurvey';
      exportAsExcelFile(_sheets, _excelFinalData, _fileName);
    }
  }

  exportReportesGeneralesUserPDF(): void {
    /*REPORTES GENERALES:
     *Cantidad de encuestas creadas
     * cantidad de personas que han completado las encuestas
     *Cantidad de encuestas por estado
     * Cantidad de encuestas por acceso
     * */

    if (!this.reportsGeneral) {
      const doc = new jsPDF();
      const _fileName = 'reportes_generales_encuestas_datasurvey';
      let _docData, _headers, _docHeaders, _docTitle;

      debugger;
      const _reporteEncuestasCreadas = [{ total_encuestas_creadas: this.cantEncuestas!.toString() }];
      debugger;

      _docData = generatePDFTableData(_reporteEncuestasCreadas);
      _headers = ['total_encuestas_creadas'];
      _docHeaders = createPDFTableHeaders(_headers);
      _docTitle = 'Reporte General Cantidad Encuestas Creadas';

      generatePDFTable(doc, _docData, _docHeaders, _docTitle);
      doc.addPage('p');

      const _reporteUsuariosCompletadas = [{ total_usuarios_completados: this.cantPersonas.toString() }];
      _docData = generatePDFTableData(_reporteUsuariosCompletadas);
      _headers = ['total_usuarios_completados'];
      _docHeaders = createPDFTableHeaders(_headers);
      _docTitle = 'Reporte General Cantidad Usuarios';

      generatePDFTable(doc, _docData, _docHeaders, _docTitle);
      doc.addPage('p');

      const _reporteEncuestasEstado = [
        {
          total_borrador: this.cantPersonas.toString(),
          total_activas: this.cantActivas.toString(),
          total_finalizadas: this.cantFinalizadas.toString(),
        },
      ];
      _docData = generatePDFTableData(_reporteEncuestasEstado);
      _headers = ['total_borrador', 'total_activas', 'total_finalizadas'];
      _docHeaders = createPDFTableHeaders(_headers);
      _docTitle = 'Reporte General Cantidad Encuestas Por Estado';

      generatePDFTable(doc, _docData, _docHeaders, _docTitle);
      doc.addPage('p');

      const _reporteEncuestasAcceso = [{ total_publicas: this.cantPublicas.toString(), total_privadas: this.cantPrivadas.toString() }];
      _docData = generatePDFTableData(_reporteEncuestasAcceso);
      _headers = ['total_publicas', 'total_privadas'];
      _docHeaders = createPDFTableHeaders(_headers);
      _docTitle = 'Reporte General Cantidad Encuestas Por Acceso';

      generatePDFTable(doc, _docData, _docHeaders, _docTitle);
      doc.addPage('p');

      saveGeneratedPDF(doc, _fileName);
    } else {
      /*REPORTES POR ENCUESTA:
       * Nombre encuesta
       * categoria encuesta
       * calificacion encuesta
       *
       * preguntas:
       * contenido/ cantidad*/

      const doc = new jsPDF();
      const _fileName = 'reportes_detalles_encuestas_datasurvey';
      let _docData, _headers, _docHeaders, _docTitle;

      const _reporteDatosEncuesta = [
        {
          nombre_encuesta: this.encuesta?.nombre,
          categoria_encuesta: this.encuesta?.categoria?.nombre,
          calificacion_encuesta: this.encuesta?.calificacion!.toString(),
        },
      ];

      _docData = generatePDFTableData(_reporteDatosEncuesta);
      _headers = ['nombre_encuesta', 'categoria_encuesta', 'calificacion_encuesta'];
      _docHeaders = createPDFTableHeaders(_headers);
      _docTitle = 'Reporte Detalle Encuesta';

      generatePDFTable(doc, _docData, _docHeaders, _docTitle);
      doc.addPage('l');

      const _reporteContenidoPreguntasCerradas: any[] = [];
      const _reporteContenidoPreguntasAbiertas: any[] = [];

      this.ePreguntas!.forEach((pregunta: any, index) => {
        debugger;
        let _report: any = {};
        let _reportAbierta: any = {};

        if (!pregunta.tipo) {
          this.respuestaAbierta!.forEach((respuesta: any) => {
            if (respuesta.epreguntaAbierta?.id == pregunta.id) {
              _reportAbierta['pregunta_abierta'] = 'Contenido';
              _reportAbierta['contenido'] = respuesta.respuesta;
              _reporteContenidoPreguntasAbiertas.push(_reportAbierta);
            }
          });
        } else {
          debugger;

          this.ePreguntasOpciones!.forEach((respuesta: any, index) => {
            console.log(respuesta.epreguntaCerrada);

            if (respuesta[index].epreguntaCerrada.id == pregunta.id) {
              _report['opcion_pregunta'] = respuesta[index].nombre;
              _report['cantidad'] = respuesta[index].cantidad.toString();
              _reporteContenidoPreguntasCerradas.push(_report);
            }
          });
        }
      });

      _docData = generatePDFTableData(_reporteContenidoPreguntasCerradas);
      _headers = ['opcion_pregunta', 'cantidad'];
      _docHeaders = createPDFTableHeaders(_headers);
      _docTitle = 'Reporte de Cantidad Seleccion Opciones Cerradas';

      generatePDFTable(doc, _docData, _docHeaders, _docTitle);
      doc.addPage('l');

      _docData = generatePDFTableData(_reporteContenidoPreguntasAbiertas);
      _headers = ['pregunta_abierta', 'contenido'];
      _docHeaders = createPDFTableHeaders(_headers);
      _docTitle = 'Reporte Respuesta Pregunta Abierta';

      generatePDFTable(doc, _docData, _docHeaders, _docTitle);
      doc.addPage('l');

      saveGeneratedPDF(doc, _fileName);
    }
  }
}
