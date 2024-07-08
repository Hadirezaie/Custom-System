import trader from 'app/entities/trader/trader.reducer';
import imei from 'app/entities/imei/imei.reducer';
import device from 'app/entities/device/device.reducer';
import tarif from 'app/entities/tarif/tarif.reducer';
import customCharges from 'app/entities/custom-charges/custom-charges.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  trader,
  imei,
  device,
  tarif,
  customCharges,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
