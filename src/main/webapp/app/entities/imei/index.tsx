import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Imei from './imei';
import ImeiDetail from './imei-detail';
import ImeiUpdate from './imei-update';
import ImeiDeleteDialog from './imei-delete-dialog';

const ImeiRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Imei />} />
    <Route path="new" element={<ImeiUpdate />} />
    <Route path=":id">
      <Route index element={<ImeiDetail />} />
      <Route path="edit" element={<ImeiUpdate />} />
      <Route path="delete" element={<ImeiDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ImeiRoutes;
