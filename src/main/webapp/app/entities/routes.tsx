import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BonusMalus from './bonus-malus';
import Film from './film';
import FilmPersonaggio from './film-personaggio';
import Lega from './lega';
import Personaggio from './personaggio';
import UserExtended from './user-extended';
import Squadra from './squadra';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}bonus-malus`} component={BonusMalus} />
        <ErrorBoundaryRoute path={`${match.url}film`} component={Film} />
        <ErrorBoundaryRoute path={`${match.url}film-personaggio`} component={FilmPersonaggio} />
        <ErrorBoundaryRoute path={`${match.url}lega`} component={Lega} />
        <ErrorBoundaryRoute path={`${match.url}personaggio`} component={Personaggio} />
        <ErrorBoundaryRoute path={`${match.url}user-extended`} component={UserExtended} />
        <ErrorBoundaryRoute path={`${match.url}squadra`} component={Squadra} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
