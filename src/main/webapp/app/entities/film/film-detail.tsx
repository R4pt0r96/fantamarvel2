import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './film.reducer';

export const FilmDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const filmEntity = useAppSelector(state => state.film.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="filmDetailsHeading">Film</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{filmEntity.id}</dd>
          <dt>
            <span id="titolo">Titolo</span>
          </dt>
          <dd>{filmEntity.titolo}</dd>
          <dt>
            <span id="dataUscita">Data Uscita</span>
          </dt>
          <dd>{filmEntity.dataUscita ? <TextFormat value={filmEntity.dataUscita} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="dataFineIscrizione">Data Fine Iscrizione</span>
          </dt>
          <dd>
            {filmEntity.dataFineIscrizione ? (
              <TextFormat value={filmEntity.dataFineIscrizione} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{filmEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="urlImg">Url Img</span>
          </dt>
          <dd>{filmEntity.urlImg}</dd>
          <dt>
            <span id="descrizione">Descrizione</span>
          </dt>
          <dd>{filmEntity.descrizione}</dd>
          <dt>User Extended</dt>
          <dd>
            {filmEntity.userExtendeds
              ? filmEntity.userExtendeds.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.username}</a>
                    {filmEntity.userExtendeds && i === filmEntity.userExtendeds.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/film" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/film/${filmEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FilmDetail;
