import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CustomCharges from './custom-charges';
import CustomChargesDetail from './custom-charges-detail';
import CustomChargesUpdate from './custom-charges-update';
import CustomChargesDeleteDialog from './custom-charges-delete-dialog';

const CustomChargesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CustomCharges />} />
    <Route path="new" element={<CustomChargesUpdate />} />
    <Route path=":id">
      <Route index element={<CustomChargesDetail />} />
      <Route path="edit" element={<CustomChargesUpdate />} />
      <Route path="delete" element={<CustomChargesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CustomChargesRoutes;
