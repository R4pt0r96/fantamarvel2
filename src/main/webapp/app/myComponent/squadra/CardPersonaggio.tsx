import React from 'react';
import { Button } from 'reactstrap';

import './CardPersonaggio.scss';

const CardPersonaggio = ({ personaggio, gettoniSquadra, addPersonaggio }) => {
  let classeImg = 'card_personaggio_img';
  if (personaggio.costo > gettoniSquadra) {
    classeImg = 'card_personaggio_img grey';
  }
  return (
    <div className="card_personaggio">
      <img className={classeImg} src={personaggio.personaggio.urlImg} />
      <b>{personaggio.personaggio.nome}</b>
      <p>$ {personaggio.costo}</p>
      {personaggio.costo <= gettoniSquadra ? (
        <Button color="success" size="sm" className="card_personaggio_btn" onClick={() => addPersonaggio(personaggio)}>
          Aggiungi
        </Button>
      ) : null}
    </div>
  );
};

export default CardPersonaggio;
