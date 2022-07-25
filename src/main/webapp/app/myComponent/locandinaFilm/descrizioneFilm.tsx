import React from 'react';
import ConvertitoreDate from '../utility/ConvertitoreDate';

import './descrizioneFilm.scss';

const DescrizioneFilm = props => {
  const dataUscita = ConvertitoreDate(props.data);

  return (
    <div className="descrizioneFilmContainer">
      <h1 className="titoloFilm" data-heading="thor">
        {props.titolo}
      </h1>
      <p id="dataUscita">Al cinema dal: {dataUscita}</p>
      <p id="descrizioneFilm">{props.descrizione}</p>
    </div>
  );
};

export default DescrizioneFilm;
