import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Lega from './lega';
import LegaDetail from './lega-detail';
import LegaUpdate from './lega-update';
import LegaDeleteDialog from './lega-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LegaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LegaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LegaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Lega} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LegaDeleteDialog} />
  </>
);

export default Routes;
