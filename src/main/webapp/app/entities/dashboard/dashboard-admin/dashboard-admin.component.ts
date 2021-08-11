import { Component, OnInit } from '@angular/core';
import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';
import { jsPDF } from 'jspdf';
import { exportAsExcelFile } from '../export/export_excel';
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
  faWallet = faWallet;
  faUsers = faUsers;
  faUsersSlash = faUsersSlash;

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

  loadAll() {
    this.cargarGananciasTotales();
    this.cargarCantidadUsuarios();
    this.cargarEncuestas();
  }

  cargarGananciasTotales() {
    this.facturaService.query().subscribe(
      res => {
        const tempFacturas = res.body;
        tempFacturas?.forEach(f => {
          if (f.costo != undefined) {
            this.gananciasTotales += f.costo;
          }
        });
      },
      () => {}
    );
  }

  cargarCantidadUsuarios() {
    this.usuarioExtraService.query().subscribe(res => {
      const tmpUsuarios = res.body;
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
        this.encuestas = tmpEncuestas?.filter(e => e.estado === 'ACTIVE' || e.estado === 'FINISHED');
      });
  }

  cargarCategorias() {
    this.categoriaService
      .query()
      .pipe(finalize(() => this.acomodarMesesYAnnos()))
      .subscribe(res => {
        const tmpCategorias = res.body;
        this.categorias = tmpCategorias?.filter(c => c.estado === 'ACTIVE');
        let cantPublicadas = 0;
        let cantFinalizadas = 0;
        const publicadas: number[] | null = [];
        const finalizadas: number[] | null = [];
        this.categorias?.forEach(c => {
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
    */

    const _sheets = ['reportes generales'];
    const _reporteUsuarios = { usuarios_activos: 100, usuarios_bloqueados: 50 };

    const _excelFinalData = [_reporteUsuarios];
    const _fileName = 'reporte_general';
    exportAsExcelFile(_sheets, _excelFinalData, _fileName);
  }

  exportReportesGeneralesAdministradorPDF(): void {
    /*
      Cantidad de usuarios activos
      Cantidad de usuarios bloqueados
      Cantidad de encuestas publicadas por categoría
      Cantidad de encuestas finalizadas por categoría
      Cantidad de encuestas publicadas por mes y año
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
