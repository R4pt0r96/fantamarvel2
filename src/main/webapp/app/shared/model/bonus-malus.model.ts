import { IFilm } from 'app/shared/model/film.model';
import { IPersonaggio } from 'app/shared/model/personaggio.model';

export interface IBonusMalus {
  id?: number;
  descrizione?: string;
  punti?: number;
  film?: IFilm;
  personaggios?: IPersonaggio[] | null;
}

export const defaultValue: Readonly<IBonusMalus> = {};
