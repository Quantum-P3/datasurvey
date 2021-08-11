import { jsPDF } from 'jspdf';
import { generateFileName } from './export_common';

const PDF_EXTENSION: string = '.pdf';

export const generatePDFTableData = (data: any): any => {
  const result: any = [];

  data.forEach((item: any) => {
    result.push(Object.assign({}, item));
  });

  return result;
};

export const createPDFTableHeaders = (keys: any): any[] => {
  let result = [];
  for (let i = 0; i < keys.length; i += 1) {
    result.push({
      id: keys[i],
      name: keys[i],
      prompt: keys[i],
      width: 65,
      align: 'center',
      padding: 0,
    });
  }
  return result;
};

export const generatePDFTable = (doc: jsPDF, _docData: any, _docHeaders: string[]): void => {
  doc.table(1, 1, _docData, _docHeaders, { autoSize: true });
  const generatedFileName = generateFileName('reporte_general', PDF_EXTENSION);
  doc.save(generatedFileName);
};
