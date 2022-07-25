import React from 'react';

import './locandinaFilm.css';

const LocandinaFilm = props => {
  return (
    <img
      src={props.immagine}
      className="locandinaFilm"
      // style={{
      //   backgroundImage: `url("${props.immagine}")`,
      // }}
    ></img>
  );
};

export default LocandinaFilm;
