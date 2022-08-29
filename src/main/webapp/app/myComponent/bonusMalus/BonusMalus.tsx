import React, { useEffect, useRef } from 'react';
import { Alert } from 'reactstrap';
import BonusMalusItem from './BonusMalusItem';

const BonusMalusComponent = props => {
  const filmActiveBonusMalus = props.bonusMalusList.filter(item => item?.film?.id === props.idFilm);
  const refBonusMalus = useRef(null);

  useEffect(() => {
    refBonusMalus.current.scrollIntoView({ block: 'center', behavior: 'smooth' });
  }, []);

  return (
    <div ref={refBonusMalus}>
      {filmActiveBonusMalus[0] ? (
        filmActiveBonusMalus.map(item => {
          return <BonusMalusItem key={item.id} descrizione={item.descrizione} punti={item.punti} />;
        })
      ) : (
        <Alert color="info">Nessun Bonus Malus al momento</Alert>
      )}
    </div>
  );
};

export default BonusMalusComponent;
