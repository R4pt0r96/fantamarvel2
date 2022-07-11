import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './film-personaggio.reducer';

export const FilmPersonaggioDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const filmPersonaggioEntity = useAppSelector(state => state.filmPersonaggio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="filmPersonaggioDetailsHeading">FilmPersonaggio</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{filmPersonaggioEntity.id}</dd>
          <dt>
            <span id="costo">Costo</span>
          </dt>
          <dd>{filmPersonaggioEntity.costo}</dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{filmPersonaggioEntity.isActive ? 'true' : 'false'}</dd>
          <dt>Film</dt>
          <dd>{filmPersonaggioEntity.film ? filmPersonaggioEntity.film.titolo : ''}</dd>
          <dt>Personaggio</dt>
          <dd>{filmPersonaggioEntity.personaggio ? filmPersonaggioEntity.personaggio.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/film-personaggio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/film-personaggio/${filmPersonaggioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FilmPersonaggioDetail;
