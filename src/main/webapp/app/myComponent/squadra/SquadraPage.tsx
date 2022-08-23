import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getEntitiesFilmPersonaggio, getEntity } from 'app/entities/film-personaggio/film-personaggio.reducer';
import { getEntities as getEntitiesUserExt } from 'app/entities/user-extended/user-extended.reducer';
import React, { useEffect, useState } from 'react';
import { getUrlParameter } from 'react-jhipster';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Card, CardBody, CardGroup, CardImg, CardSubtitle, CardTitle, Col, Row } from 'reactstrap';
import { getEntity as getEntityFilm } from 'app/entities/film/film.reducer';
import {
  createEntity as createEntitySquadra,
  getEntity as getEntitySquadra,
  getEntities as getEntitiesSquadra,
} from '../../entities/squadra/squadra.reducer';

import './SquadraPage.scss';

const SquadraPage = (props: RouteComponentProps<{ idfilm: any }>) => {
  const dispatch = useAppDispatch();
  const filmPersonaggioList = useAppSelector(state => state.filmPersonaggio.entities);
  const loadingFilmPersonaggio = useAppSelector(state => state.filmPersonaggio.loading);
  const account = useAppSelector(state => state.authentication.account);
  const idFilm = getUrlParameter('idfilm', props.location.search);
  const userExt = useAppSelector(state => state.userExtended.entities).filter(user => user.user.id == account.id);
  const filmEntity = useAppSelector(state => state.film.entity);
  const filmList = useAppSelector(state => state.film.entities);
  const squadraEntity = useAppSelector(state => state.squadra.entity);
  const loadingFilm = useAppSelector(state => state.film.loading);
  const loadingUser = useAppSelector(state => state.userExtended.loading);
  const loadingSquadre = useAppSelector(state => state.squadra.loading);
  const loadingUserExt = useAppSelector(state => state.userExtended.loading);
  const squadraList = useAppSelector(state => state.squadra.entities);

  useEffect(() => {
    dispatch(getEntitiesFilmPersonaggio({}));
  }, []);

  // const saveEntity = () => {
  //   const entity = {
  //     ...squadraEntity,
  //     film: filmEntity,
  //     userExtended: userExt[0],
  //   };

  //   dispatch(createEntitySquadra(entity));
  // };

  // if (!loadingFilm && !loadingUser && !loadingSquadre && !loadingUserExt) {
  //   let squadraExist = null;
  //   squadraExist = squadraList.filter(sqd => sqd.film.id == idFilm && sqd.userExtended.id == userExt.id);
  //   if (squadraExist[0]?.film.id != idFilm) {
  //     console.log(squadraExist);

  //     saveEntity();
  //   }
  // }

  return (
    <div>
      <Row>
        <Col md="8">
          <CardGroup>
            <Card>
              <CardImg alt="Card image cap" src="https://picsum.photos/318/180" top width="100%" />
              <CardBody>
                <CardTitle tag="h5">Card title</CardTitle>
                <CardSubtitle className="mb-2 text-muted" tag="h6">
                  Card subtitle
                </CardSubtitle>
                <Button>Rimuovi</Button>
              </CardBody>
            </Card>
            <Card>
              <CardImg alt="Card image cap" src="https://picsum.photos/318/180" top width="100%" />
              <CardBody>
                <CardTitle tag="h5">Card title</CardTitle>
                <CardSubtitle className="mb-2 text-muted" tag="h6">
                  Card subtitle
                </CardSubtitle>
                <Button>Rimuovi</Button>
              </CardBody>
            </Card>
            <Card>
              <CardImg alt="Card image cap" src="https://picsum.photos/318/180" top width="100%" />
              <CardBody>
                <CardTitle tag="h5">Card title</CardTitle>
                <CardSubtitle className="mb-2 text-muted" tag="h6">
                  Card subtitle
                </CardSubtitle>
                <Button>Rimuovi</Button>
              </CardBody>
            </Card>
          </CardGroup>
        </Col>
        <Col md="4">info</Col>
      </Row>
    </div>
  );
};
export default SquadraPage;
