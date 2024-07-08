import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Trader from './trader';
import TraderDetail from './trader-detail';
import TraderUpdate from './trader-update';
import TraderDeleteDialog from './trader-delete-dialog';

const TraderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Trader />} />
    <Route path="new" element={<TraderUpdate />} />
    <Route path=":id">
      <Route index element={<TraderDetail />} />
      <Route path="edit" element={<TraderUpdate />} />
      <Route path="delete" element={<TraderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TraderRoutes;
