import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Personaggio from './personaggio';
import PersonaggioDetail from './personaggio-detail';
import PersonaggioUpdate from './personaggio-update';
import PersonaggioDeleteDialog from './personaggio-delete-dialog';
import PersonaggioBonusMalus from './personaggio-bonusmalus';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PersonaggioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PersonaggioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit-bonusmalus`} component={PersonaggioBonusMalus} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PersonaggioDetail} />
      <ErrorBoundaryRoute path={match.url} component={Personaggio} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PersonaggioDeleteDialog} />
  </>
);

export default Routes;
