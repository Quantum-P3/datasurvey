import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';
import { generateFileName } from './export_common';

const EXCEL_TYPE: string = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION: string = '.xlsx';

export const exportAsExcelFile = (sheetNames: string[], arrayOfData: any[], excelFileName: any) => {
  const workbook = XLSX.utils.book_new();

  arrayOfData.forEach((data, index) => {
    let sheetName = sheetNames[index];
    let worksheet = XLSX.utils.json_to_sheet([data]);
    XLSX.utils.book_append_sheet(workbook, worksheet, sheetName);
  });

  const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
  saveAsExcelFile(excelBuffer, excelFileName);
};

const saveAsExcelFile = (buffer: any, fileName: any) => {
  const data = new Blob([buffer], { type: EXCEL_EXTENSION });
  const generatedFileName = generateFileName(fileName, EXCEL_EXTENSION);

  FileSaver.saveAs(data, generatedFileName);
};
