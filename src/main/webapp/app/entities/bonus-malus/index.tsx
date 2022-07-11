import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BonusMalus from './bonus-malus';
import BonusMalusDetail from './bonus-malus-detail';
import BonusMalusUpdate from './bonus-malus-update';
import BonusMalusDeleteDialog from './bonus-malus-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BonusMalusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BonusMalusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BonusMalusDetail} />
      <ErrorBoundaryRoute path={match.url} component={BonusMalus} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BonusMalusDeleteDialog} />
  </>
);

export default Routes;
