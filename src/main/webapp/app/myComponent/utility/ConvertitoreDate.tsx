const ConvertitoreDate = (dataStringa: string) => {
  const data = new Date(dataStringa);
  const giorno = data.getDate().toString();
  const mese = data.getUTCMonth().toString();
  const anno = data.getFullYear().toString();
  const meseArray = [
    'Gennaio',
    'Febbraio',
    'Marzo',
    'Aprile',
    'Maggio',
    'Giugno',
    'Luglio',
    'Agosto',
    'Settembre',
    'Ottobre',
    'Novembre',
    'Dicembre',
  ];
  const dataCompleta = giorno + ' ' + meseArray[mese] + ' ' + anno;
  return dataCompleta;
};

export default ConvertitoreDate;
