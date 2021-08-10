import { Component, OnInit } from '@angular/core';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';

@Component({
  selector: 'jhi-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.scss'],
})
export class DashboardAdminComponent implements OnInit {
  constructor() {}

  EXCEL_TYPE: string = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
  EXCEL_EXTENSION: string = '.xlsx';

  ngOnInit(): void {}

  exportAsExcelFile = (sheetNames: string[], arrayOfData: any[], excelFileName: any) => {
    const workbook = XLSX.utils.book_new();

    arrayOfData.forEach((data, index) => {
      let sheetName = sheetNames[index];
      let worksheet = XLSX.utils.json_to_sheet([data]);
      XLSX.utils.book_append_sheet(workbook, worksheet, sheetName);
    });

    const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
    this.saveAsExcelFile(excelBuffer, excelFileName);
  };

  saveAsExcelFile = (buffer: any, fileName: any) => {
    const data = new Blob([buffer], { type: this.EXCEL_TYPE });
    FileSaver.saveAs(
      data,
      fileName +
        '_' +
        new Date().toLocaleDateString().substr(0, 10).split('/').join('-') +
        '_' +
        Math.random().toString().substring(2) +
        this.EXCEL_EXTENSION
    );
  };

  exportReportesGeneralesAdministrador(): void {
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
    this.exportAsExcelFile(_sheets, _excelFinalData, 'reporte_general');
  }
}
