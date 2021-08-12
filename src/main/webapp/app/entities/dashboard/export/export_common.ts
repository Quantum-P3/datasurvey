export const generateFileName = (fileName: string, extension: string): string => {
  return (
    fileName +
    '_' +
    new Date().toLocaleDateString().substr(0, 10).split('/').join('-') +
    '_' +
    Math.random().toString().substring(2) +
    extension
  );
};
