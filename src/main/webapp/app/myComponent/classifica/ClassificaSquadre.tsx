import React, { Fragment, useEffect, useState } from 'react';

import { getEntities as getEntitiesSquadra } from 'app/entities/squadra/squadra.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getEntitiesUserExt } from 'app/entities/user-extended/user-extended.reducer';
import { getEntities as getEntitiesBonusMalus } from 'app/entities/bonus-malus/bonus-malus.reducer';

import './ClassificaSquadre.scss';
import { Alert, Button, Card, CardBody, Modal, ModalBody, ModalFooter, ModalHeader, Table, UncontrolledCollapse } from 'reactstrap';
import { isEmpty } from 'lodash';
import BonusMalusComponent from '../bonusMalus/BonusMalus';

const ClassificaSquadre = () => {
  const dispatch = useAppDispatch();
  const listaSquadre = useAppSelector(state => state.squadra.entities);
  const listaSquadreIsLoading = useAppSelector(state => state.squadra.loading);
  const account = useAppSelector(state => state.authentication.account);
  const userExtended = useAppSelector(state => state.userExtended.entities).filter(user => user.user.id == account.id);
  const userExtendedIsLoading = useAppSelector(state => state.userExtended.loading);
  const [width, setWidth] = useState(window.innerWidth);
  const bonusMalusList = useAppSelector(state => state.bonusMalus.entities);
  const bonusMalusListIsLoading = useAppSelector(state => state.bonusMalus.loading);
  const [modal, setModal] = useState(false);
  const [filmID, setFilmID] = useState(0);
  const [personaggioID, setPersonaggioID] = useState(0);

  useEffect(() => {
    dispatch(getEntitiesSquadra({}));
    dispatch(getEntitiesUserExt({}));
    dispatch(getEntitiesBonusMalus({}));
    window.addEventListener('resize', updateWidth);
    return () => window.removeEventListener('resize', updateWidth);
  }, []);

  const updateWidth = () => {
    setWidth(window.innerWidth);
  };

  const toggle = (film_id, personaggio_id) => {
    setFilmID(film_id);
    setPersonaggioID(personaggio_id);
    setModal(!modal);
  };

  const sortArrayByPunteggio = (a, b) => {
    if (a.punteggio > b.punteggio) {
      return -1;
    } else if (a.punteggio < b.punteggio) {
      return 1;
    }
    return 0;
  };

  let listaSquadreFiltrate = [];
  if (!listaSquadreIsLoading && !bonusMalusListIsLoading) {
    listaSquadreFiltrate = listaSquadre.filter(squadra => squadra.film.isActive && squadra.isSalvata);
    listaSquadreFiltrate.sort(sortArrayByPunteggio);
  }

  let posizione = 0;

  if (!isEmpty(listaSquadreFiltrate) && !listaSquadreIsLoading && !userExtendedIsLoading && !bonusMalusListIsLoading) {
    return (
      <>
        {width > 768 ? (
          <>
            <Table borderless hover responsive className="table_classifica">
              <thead>
                <tr>
                  <th>Posizione</th>
                  <th>Username</th>
                  <th>Punteggio</th>
                  <th colSpan={3}>Squadra</th>
                </tr>
              </thead>

              <tbody>
                {listaSquadreFiltrate.map(squadra => {
                  posizione++;
                  return (
                    <tr key={squadra.id} className={userExtended[0].id == squadra.userExtended.id ? 'yourTeam' : ''}>
                      <th scope="row">{posizione}°</th>
                      <td>{squadra.userExtended.username}</td>
                      <td>{squadra.punteggio}</td>
                      {squadra.personaggios.map(pers => {
                        return (
                          <td key={pers.id}>
                            <Button
                              color={userExtended[0].id == squadra.userExtended.id ? 'danger' : 'secondary'}
                              style={{ width: '100%' }}
                              outline
                              onClick={() => {
                                toggle(squadra.film.id, pers.id);
                              }}
                            >
                              {pers.nome}
                            </Button>
                          </td>
                        );
                      })}
                    </tr>
                  );
                })}
              </tbody>
            </Table>
            <Modal
              size="lg"
              centered={true}
              scrollable={true}
              isOpen={modal}
              toggle={() => {
                toggle(filmID, personaggioID);
              }}
            >
              <ModalHeader
                toggle={() => {
                  toggle(filmID, personaggioID);
                }}
              >
                Bonus - Malus
              </ModalHeader>
              <ModalBody>
                <BonusMalusComponent
                  idFilm={filmID}
                  bonusMalusList={bonusMalusList.filter(bm => {
                    if (bm?.personaggios) {
                      for (let index = 0; index < bm.personaggios.length; index++) {
                        if (bm.personaggios[index].id == personaggioID) {
                          return bm;
                        }
                      }
                    }
                  })}
                />
              </ModalBody>
            </Modal>
          </>
        ) : (
          <div className="classifica_container">
            {listaSquadreFiltrate.map(squadra => {
              posizione++;
              return (
                <Card key={squadra.id} className="classifica_card">
                  <h1 className={userExtended[0].id == squadra.userExtended.id ? 'yourTeam' : ''}>{posizione}°</h1>
                  <p className={userExtended[0].id == squadra.userExtended.id ? 'yourTeam' : ''}>
                    <b>{squadra.userExtended.username}</b>
                  </p>
                  <p className={userExtended[0].id == squadra.userExtended.id ? 'yourTeam' : ''}>Punteggio: {squadra.punteggio}</p>
                  {squadra.personaggios.map(pers => {
                    return (
                      <Fragment key={pers.id}>
                        <Button
                          outline
                          color={userExtended[0].id == squadra.userExtended.id ? 'danger' : 'secondary'}
                          id={'toggler' + pers.id + posizione}
                          style={{ marginBottom: '1rem' }}
                        >
                          {pers.nome}
                        </Button>
                        <UncontrolledCollapse toggler={'#toggler' + pers.id + posizione}>
                          <Card>
                            <CardBody>
                              <BonusMalusComponent
                                idFilm={squadra.film.id}
                                bonusMalusList={bonusMalusList.filter(bm => {
                                  if (bm?.personaggios) {
                                    for (let index = 0; index < bm.personaggios.length; index++) {
                                      if (bm.personaggios[index].id == pers.id) {
                                        return bm;
                                      }
                                    }
                                  }
                                })}
                              />
                            </CardBody>
                          </Card>
                        </UncontrolledCollapse>
                      </Fragment>
                    );
                  })}
                </Card>
              );
            })}
          </div>
        )}
      </>
    );
  } else {
    return <Alert color="warning">Nessuna classifica al momento</Alert>;
  }
};

export default ClassificaSquadre;
