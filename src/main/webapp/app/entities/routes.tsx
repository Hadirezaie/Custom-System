import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Trader from './trader';
import Imei from './imei';
import Device from './device';
import Tarif from './tarif';
import CustomCharges from './custom-charges';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="trader/*" element={<Trader />} />
        <Route path="imei/*" element={<Imei />} />
        <Route path="device/*" element={<Device />} />
        <Route path="tarif/*" element={<Tarif />} />
        <Route path="custom-charges/*" element={<CustomCharges />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
