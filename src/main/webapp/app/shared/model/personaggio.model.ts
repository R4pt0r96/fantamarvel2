import { IBonusMalus } from 'app/shared/model/bonus-malus.model';
import { ISquadra } from 'app/shared/model/squadra.model';

export interface IPersonaggio {
  id?: number;
  nome?: string;
  description?: string | null;
  note?: string | null;
  isActive?: boolean | null;
  urlImg?: string | null;
  bonusmaluses?: IBonusMalus[] | null;
  teams?: ISquadra[] | null;
}

export const defaultValue: Readonly<IPersonaggio> = {
  isActive: false,
};
