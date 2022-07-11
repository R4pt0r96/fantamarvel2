import dayjs from 'dayjs';
import { IUserExtended } from 'app/shared/model/user-extended.model';

export interface IFilm {
  id?: number;
  titolo?: string;
  dataUscita?: string | null;
  dataFineIscrizione?: string | null;
  isActive?: boolean | null;
  urlImg?: string | null;
  descrizione?: string | null;
  userExtendeds?: IUserExtended[] | null;
}

export const defaultValue: Readonly<IFilm> = {
  isActive: false,
};
