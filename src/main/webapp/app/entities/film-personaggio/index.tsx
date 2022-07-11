import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FilmPersonaggio from './film-personaggio';
import FilmPersonaggioDetail from './film-personaggio-detail';
import FilmPersonaggioUpdate from './film-personaggio-update';
import FilmPersonaggioDeleteDialog from './film-personaggio-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FilmPersonaggioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FilmPersonaggioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FilmPersonaggioDetail} />
      <ErrorBoundaryRoute path={match.url} component={FilmPersonaggio} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FilmPersonaggioDeleteDialog} />
  </>
);

export default Routes;
