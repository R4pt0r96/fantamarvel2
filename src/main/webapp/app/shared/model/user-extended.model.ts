import { IUser } from 'app/shared/model/user.model';
import { IFilm } from 'app/shared/model/film.model';

export interface IUserExtended {
  id?: number;
  username?: string | null;
  provincia?: string | null;
  note1?: string | null;
  note2?: string | null;
  note3?: string | null;
  user?: IUser | null;
  films?: IFilm[] | null;
}

export const defaultValue: Readonly<IUserExtended> = {};
