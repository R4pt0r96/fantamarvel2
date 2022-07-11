import bonusMalus from 'app/entities/bonus-malus/bonus-malus.reducer';
import film from 'app/entities/film/film.reducer';
import filmPersonaggio from 'app/entities/film-personaggio/film-personaggio.reducer';
import lega from 'app/entities/lega/lega.reducer';
import personaggio from 'app/entities/personaggio/personaggio.reducer';
import userExtended from 'app/entities/user-extended/user-extended.reducer';
import squadra from 'app/entities/squadra/squadra.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  bonusMalus,
  film,
  filmPersonaggio,
  lega,
  personaggio,
  userExtended,
  squadra,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
