import { Component, OnInit } from '@angular/core';
import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';
import { jsPDF } from 'jspdf';
import { exportAsExcelFile, exportAsExcelTable } from '../export/export_excel';
import { generatePDFTableData, createPDFTableHeaders, generatePDFTable } from '../export/export_pdf';

import { FacturaService } from '../../factura/service/factura.service';
import { UsuarioExtraService } from '../../usuario-extra/service/usuario-extra.service';
import { CategoriaService } from '../../categoria/service/categoria.service';
import { EncuestaService } from '../../encuesta/service/encuesta.service';

import { ICategoria } from '../../categoria/categoria.model';
import { IEncuesta } from '../../encuesta/encuesta.model';
import { finalize } from 'rxjs/operators';

import * as Chartist from 'chartist';

import { faWallet, faUsers, faUsersSlash } from '@fortawesome/free-solid-svg-icons';
import { IUsuarioExtra } from '../../usuario-extra/usuario-extra.model';
import { IUser } from '../../user/user.model';

@Component({
  selector: 'jhi-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.scss'],
})
export class DashboardAdminComponent implements OnInit {
  cantUsuarioActivos: number | undefined = 0;
  cantUsuarioBloqueados: number | undefined = 0;
  encuestasPublicadasCategoria: number[] = [];
  encuestasFinalzadasCategoria: number[] = [];
  encuestasPublicadasMesAnno: number[] = [];
  listaMesesAnnos: string[] = [];
  gananciasTotales: number = 0;
  categorias: ICategoria[] | undefined = [];
  encuestas: IEncuesta[] | undefined = [];
  usuarios: IUsuarioExtra[] | undefined = [];
  faWallet = faWallet;
  faUsers = faUsers;
  faUsersSlash = faUsersSlash;
  encuestasPublicadas: number = 0;
  encuestasFinalizadas: number = 0;
  encuestasBorrador: number = 0;
  encuestasCompletadas: number = 0;
  encuestasUsuario: number[] = [];
  encuestasUsuarioPublicadas: number[] = [];
  encuestasUsuarioFinalizadas: number[] = [];
  encuestasUsuarioBorrador: number[] = [];
  encuestasUsuarioCompletadas: number[] = [];
  usuariosGenerales: IUser[] | null = [];

  reportsGeneral = false;
  reportForUsers = true;

  chartFechas = [];

  constructor(
    protected facturaService: FacturaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected encuestaService: EncuestaService,
    protected categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICategoria): number {
    return item.id!;
  }

  trackIdUsuario(_index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  cambiarVista() {
    if (this.reportsGeneral) {
      this.reportsGeneral = false;
      this.reportForUsers = true;
    } else if (this.reportForUsers) {
      this.reportsGeneral = true;
      this.reportForUsers = false;
    }
  }

  loadAll() {
    this.cargarGananciasTotales();
    this.cargarUsers();
  }

  cargarGananciasTotales() {
    this.facturaService.query().subscribe(res => {
      const tempFacturas = res.body;
      tempFacturas?.forEach(f => {
        if (f.costo != undefined) {
          this.gananciasTotales += f.costo;
        }
      });
    });
  }

  cargarUsers() {
    this.usuarioExtraService
      .retrieveAllPublicUsers()
      .pipe(finalize(() => this.cargarCantidadUsuarios()))
      .subscribe(res => {
        res.forEach(user => {
          let rolList: string[] | undefined;
          rolList = user.authorities;
          let a = rolList?.pop();
          if (a == 'ROLE_ADMIN') {
            user.authorities = ['Admin'];
          } else if (a == 'ROLE_USER') {
            user.authorities = ['Usuario'];
          }
        });
        this.usuariosGenerales = res;
      });
  }

  cargarCantidadUsuarios() {
    this.usuarioExtraService
      .query()
      .pipe(finalize(() => this.cargarEncuestas()))
      .subscribe(res => {
        const tmpUsuarios = res.body;

        if (tmpUsuarios) {
          tmpUsuarios.forEach(u => {
            u.user = this.usuariosGenerales?.find(g => g.id == u.user?.id);
          });
        }
        this.usuarios = tmpUsuarios?.filter(u => u.user?.authorities && u.user?.authorities[0] === 'Usuario');
        this.cantUsuarioActivos = tmpUsuarios?.filter(u => u.estado === 'ACTIVE').length;
        this.cantUsuarioBloqueados = tmpUsuarios?.filter(u => u.estado === 'SUSPENDED').length;
      });
  }

  cargarEncuestas() {
    this.encuestaService
      .query()
      .pipe(finalize(() => this.cargarCategorias()))
      .subscribe(res => {
        const tmpEncuestas = res.body;
        this.encuestas = tmpEncuestas?.filter(e => e.estado === 'ACTIVE' || e.estado === 'FINISHED' || e.estado === 'DRAFT');
        if (tmpEncuestas) {
          this.encuestasPublicadas = tmpEncuestas.filter(e => e.estado === 'ACTIVE').length;
          this.encuestasFinalizadas = tmpEncuestas.filter(e => e.estado === 'FINISHED').length;
          this.encuestasBorrador = tmpEncuestas.filter(e => e.estado === 'DRAFT').length;
          let cantidadCompletadas: number = 0;
          tmpEncuestas
            .filter(e => e.estado === 'ACTIVE')
            .forEach(e => {
              const _contadorCompletadas = e.calificacion;
              cantidadCompletadas = cantidadCompletadas + (Number(_contadorCompletadas?.toString().split('.')[1]) - 1);
            });
          this.encuestasCompletadas = cantidadCompletadas;

          //reportes generales de todos los usuarios
          const publicadasUser: number[] | null = [];
          const finalizadasUser: number[] | null = [];
          const borradoresUser: number[] | null = [];
          const encuestasUser: number[] | null = [];
          const encuestasCompletadasUser: number[] | null = [];

          if (this.usuarios) {
            this.usuarios.forEach(u => {
              let cantEncuestas = 0;
              let cantPublicadas = 0;
              let cantFinalizadas = 0;
              let cantBorradores = 0;
              cantEncuestas = tmpEncuestas.filter(e => e.estado !== 'DELETED' && e.usuarioExtra?.id === u.id).length;
              cantPublicadas = tmpEncuestas.filter(e => e.estado === 'ACTIVE' && e.usuarioExtra?.id === u.id).length;
              cantFinalizadas = tmpEncuestas.filter(e => e.estado === 'FINISHED' && e.usuarioExtra?.id === u.id).length;
              cantBorradores = tmpEncuestas.filter(e => e.estado === 'DRAFT' && e.usuarioExtra?.id === u.id).length;

              encuestasUser.push(cantEncuestas);
              borradoresUser.push(cantBorradores);
              publicadasUser.push(cantPublicadas);
              finalizadasUser.push(cantFinalizadas);

              let cantidadCompletadasUser: number = 0;
              tmpEncuestas
                .filter(e => e.estado === 'ACTIVE' && e.usuarioExtra?.id === u.id)
                .forEach(e => {
                  const _contadorCompletadas = e.calificacion;
                  cantidadCompletadasUser = cantidadCompletadasUser + (Number(_contadorCompletadas?.toString().split('.')[1]) - 1);
                });
              encuestasCompletadasUser.push(cantidadCompletadasUser);
            });
            this.encuestasUsuarioCompletadas = encuestasCompletadasUser;
            this.encuestasUsuario = encuestasUser;
            this.encuestasUsuarioBorrador = borradoresUser;
            this.encuestasUsuarioPublicadas = publicadasUser;
            this.encuestasUsuarioFinalizadas = finalizadasUser;
          }
        }
      });
  }

  cargarCategorias() {
    this.categoriaService
      .query()
      .pipe(finalize(() => this.acomodarMesesYAnnos()))
      .subscribe(res => {
        const tmpCategorias = res.body;
        this.categorias = tmpCategorias?.filter(c => c.estado === 'ACTIVE');
        const publicadas: number[] | null = [];
        const finalizadas: number[] | null = [];
        this.categorias?.forEach(c => {
          let cantPublicadas = 0;
          let cantFinalizadas = 0;
          this.encuestas?.forEach(e => {
            if (e.categoria?.id === c.id && e.estado === 'ACTIVE') {
              cantPublicadas = cantPublicadas + 1;
            }
            if (e.categoria?.id === c.id && e.estado === 'FINISHED') {
              cantFinalizadas = cantFinalizadas + 1;
            }
          });
          publicadas.push(cantPublicadas);
          finalizadas.push(cantFinalizadas);
        });
        this.encuestasPublicadasCategoria = publicadas;
        this.encuestasFinalzadasCategoria = finalizadas;
      });
  }

  acomodarMesesYAnnos() {
    const fechas: string[] | null = [];
    const cantEncuestasFechas: number[] | null = [];
    var encuestasPublicadas = this.encuestas?.filter(e => e.estado === 'ACTIVE');
    if (encuestasPublicadas) {
      encuestasPublicadas = this.ordenarFechas(encuestasPublicadas);
      encuestasPublicadas.forEach(e => {
        if (e.fechaPublicacion) {
          let fecha = this.formatoFecha(e.fechaPublicacion);
          if (!fechas.includes(fecha)) {
            fechas.push(fecha);
          }
        }
      });
      this.listaMesesAnnos = fechas;

      this.listaMesesAnnos.forEach(f => {
        let contEncuestaDeFecha = 0;
        if (encuestasPublicadas) {
          encuestasPublicadas.forEach(e => {
            if (e.fechaPublicacion) {
              let fecha = this.formatoFecha(e.fechaPublicacion);
              if (f === fecha) {
                contEncuestaDeFecha++;
              }
            }
          });
        }
        cantEncuestasFechas.push(contEncuestaDeFecha);
      });
      this.encuestasPublicadasMesAnno = cantEncuestasFechas;
    }
    this.llenarGraficoEncuestasXFechas();
  }

  llenarGraficoEncuestasXFechas() {
    if (this.listaMesesAnnos && this.encuestasPublicadasMesAnno) {
      var data = {
        // A labels array that can contain any sort of values
        labels: this.listaMesesAnnos,
        // Our series array that contains series objects or in this case series data arrays
        series: [this.encuestasPublicadasMesAnno],
      };
      var options = {
        low: 0,
        showArea: true,
        showLabel: true,
        axisY: {
          onlyInteger: true,
        },
      };
      new Chartist.Line('.ct-chart', data, options);
    }
  }

  formatoFecha(fecha: any): string {
    return fecha.month() + 1 + '/' + fecha.year();
  }

  ordenarFechas(encuestasPublicadas: IEncuesta[]): IEncuesta[] {
    if (encuestasPublicadas) {
      encuestasPublicadas.sort((e1, e2) => {
        if (e1.fechaPublicacion && e2.fechaPublicacion) {
          return e1.fechaPublicacion < e2.fechaPublicacion ? -1 : e1.fechaPublicacion > e2.fechaPublicacion ? 1 : 0;
        }
        return 0;
      });
    }
    return encuestasPublicadas;
  }

  exportReportesGeneralesAdministradorExcel(): void {
    /*
      Cantidad de usuarios activos
      Cantidad de usuarios bloqueados
      Cantidad de encuestas publicadas por categoría
      Cantidad de encuestas finalizadas por categoría
      Cantidad de encuestas publicadas por mes y año

      Cantidad de encuestas
      Cantidad de personas que han completado sus encuestas
      Cantidad de encuestas activas
      Cantidad de encuestas finalizadas
      Cantidad de comentarios de retroalimentación
    */

    const _sheets = ['reportes generales', 'enc. publicadas', 'enc. publicadas categoría', 'enc. finalizadas categoría'];

    const _reporteUsuarios = [
      {
        ganancias_plantillas: this.gananciasTotales,
        usuarios_activos: this.cantUsuarioActivos,
        usuarios_bloqueados: this.cantUsuarioBloqueados,
      },
    ];

    // listaMesesAnnos
    // encuestasPublicadasMesAnno
    const _reporteEncuestasPublicadas: any[] = [];
    this.listaMesesAnnos.forEach((date: any, index) => {
      let _report: any = {};
      _report['fecha'] = date;
      _report['cantidad'] = this.encuestasPublicadasMesAnno[index];
      _reporteEncuestasPublicadas.push(_report);
    });

    // this.categorias
    // this.encuestasPublicadasCategoria
    const _reporteCantidadEncuestasPublicadasCategoria: any[] = [];
    this.categorias!.forEach((categoria: any, index) => {
      let _report: any = {};
      _report['categoria'] = categoria.nombre;
      _report['cantidad'] = this.encuestasPublicadasCategoria[index];
      _reporteCantidadEncuestasPublicadasCategoria.push(_report);
    });

    // this.categorias
    // this.encuestasFinalzadasCategoria
    const _reporteCantidadEncuestasFinalizadasCategoria: any[] = [];
    this.categorias!.forEach((categoria: any, index) => {
      let _report: any = {};
      _report['categoria'] = categoria.nombre;
      _report['cantidad'] = this.encuestasFinalzadasCategoria[index];
      _reporteCantidadEncuestasFinalizadasCategoria.push(_report);
    });

    // exportAsExcelTable();

    const _excelFinalData = [
      _reporteUsuarios,
      _reporteEncuestasPublicadas,
      _reporteCantidadEncuestasPublicadasCategoria,
      _reporteCantidadEncuestasFinalizadasCategoria,
    ];
    const _fileName = 'reportes_datasurvey';
    exportAsExcelFile(_sheets, _excelFinalData, _fileName);
  }

  exportReportesGeneralesAdministradorPDF(): void {
    /*
      Cantidad de usuarios activos
      Cantidad de usuarios bloqueados
      Cantidad de encuestas publicadas por categoría
      Cantidad de encuestas finalizadas por categoría
      Cantidad de encuestas publicadas por mes y año

      Cantidad de encuestas
      Cantidad de personas que han completado sus encuestas
      Cantidad de encuestas activas
      Cantidad de encuestas finalizadas
      Cantidad de comentarios de retroalimentación

    */

    const doc = new jsPDF();

    const _reporteUsuarios = [{ usuarios_activos: '100', usuarios_bloqueados: '50' }];
    const _docData = generatePDFTableData(_reporteUsuarios);

    const _headers = ['usuarios_activos', 'usuarios_bloqueados'];
    const _docHeaders = createPDFTableHeaders(_headers);
    const _fileName = 'reporte_general';
    const _docTitle = 'Reportes Generales de la Aplicación';

    generatePDFTable(doc, _docData, _docHeaders, _fileName, _docTitle);
  }
}
