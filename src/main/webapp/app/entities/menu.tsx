import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/trader">
        Trader
      </MenuItem>
      <MenuItem icon="asterisk" to="/imei">
        Imei
      </MenuItem>
      <MenuItem icon="asterisk" to="/device">
        Device
      </MenuItem>
      <MenuItem icon="asterisk" to="/tarif">
        Tarif
      </MenuItem>
      <MenuItem icon="asterisk" to="/custom-charges">
        Custom Charges
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
