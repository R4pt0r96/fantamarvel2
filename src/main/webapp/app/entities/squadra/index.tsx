import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Squadra from './squadra';
import SquadraDetail from './squadra-detail';
import SquadraUpdate from './squadra-update';
import SquadraDeleteDialog from './squadra-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SquadraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SquadraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SquadraDetail} />
      <ErrorBoundaryRoute path={match.url} component={Squadra} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SquadraDeleteDialog} />
  </>
);

export default Routes;
