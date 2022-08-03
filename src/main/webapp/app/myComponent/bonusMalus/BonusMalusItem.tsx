import React from 'react';
import { Col, Row } from 'reactstrap';

import './BonusMalusItem.scss';

const BonusMalusItem = props => {
  return (
    <Row className={props.punti >= 0 ? 'item_container bonus_item_container' : 'item_container malus_item_container'}>
      <Col md="1" className="bonusmalus_punti">
        <h4> {props.punti >= 0 ? `+${props.punti}` : `${props.punti}`} </h4>
      </Col>
      <Col md="11" className="bonusmalus_descrizione">
        <p>{props.descrizione}</p>
      </Col>
    </Row>
  );
};

export default BonusMalusItem;
