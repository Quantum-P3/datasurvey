import { Component, OnInit } from '@angular/core';
import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';
import { jsPDF } from 'jspdf';
import { exportAsExcelFile } from '../export/export_excel';
import { generatePDFTableData, createPDFTableHeaders, generatePDFTable } from '../export/export_pdf';

@Component({
  selector: 'jhi-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.scss'],
})
export class DashboardAdminComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

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
    exportAsExcelFile(_sheets, _excelFinalData, 'reporte_general');
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

    generatePDFTable(doc, _docData, _docHeaders);
  }
}
