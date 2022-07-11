export interface ILega {
  id?: number;
  codice?: string;
  descrizione?: string | null;
  isPrivate?: boolean | null;
  nome?: string;
}

export const defaultValue: Readonly<ILega> = {
  isPrivate: false,
};
