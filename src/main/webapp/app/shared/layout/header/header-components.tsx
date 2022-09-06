import React from 'react';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/fantamarvel_logo.jpg" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    {/* <span className="brand-title">FANTAMARVEL</span> */}
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>Home</span>
    </NavLink>
  </NavItem>
);

export const ClassificaSquadreNavLink = () => (
  <NavItem>
    <NavLink tag={Link} to="/classificaSquadre" className="d-flex align-items-center">
      <FontAwesomeIcon icon="th-list" />
      <span>Classifica</span>
    </NavLink>
  </NavItem>
);
