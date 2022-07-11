import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/bonus-malus">
        Bonus Malus
      </MenuItem>
      <MenuItem icon="asterisk" to="/film">
        Film
      </MenuItem>
      <MenuItem icon="asterisk" to="/film-personaggio">
        Film Personaggio
      </MenuItem>
      <MenuItem icon="asterisk" to="/lega">
        Lega
      </MenuItem>
      <MenuItem icon="asterisk" to="/personaggio">
        Personaggio
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-extended">
        User Extended
      </MenuItem>
      <MenuItem icon="asterisk" to="/squadra">
        Squadra
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
