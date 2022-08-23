import { IFilm } from 'app/shared/model/film.model';
import { ILega } from 'app/shared/model/lega.model';
import { IUserExtended } from 'app/shared/model/user-extended.model';
import { IPersonaggio } from 'app/shared/model/personaggio.model';

export interface ISquadra {
  id?: number;
  gettoni?: number;
  isInLega?: boolean | null;
  isSalvata?: boolean;
  nome?: string | null;
  punteggio?: number | null;
  film?: IFilm | null;
  lega?: ILega | null;
  userExtended?: IUserExtended | null;
  personaggios?: IPersonaggio[] | null;
}

export const defaultValue: Readonly<ISquadra> = {
  isInLega: false,
  isSalvata: false,
  gettoni: 100,
  punteggio: 0,
};
