import { IFilm } from 'app/shared/model/film.model';
import { IPersonaggio } from 'app/shared/model/personaggio.model';

export interface IFilmPersonaggio {
  id?: number;
  costo?: number;
  isActive?: boolean | null;
  film?: IFilm | null;
  personaggio?: IPersonaggio | null;
}

export const defaultValue: Readonly<IFilmPersonaggio> = {
  isActive: false,
};
