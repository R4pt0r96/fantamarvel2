import React, { useEffect } from 'react';
import BonusMalusItem from './BonusMalusItem';

const BonusMalusComponent = props => {
  const filmActiveBonusMalus = props.bonusMalusList.filter(item => item?.film?.id === props.idFilm);

  return (
    <div>
      {filmActiveBonusMalus.map(item => {
        return <BonusMalusItem key={item.id} descrizione={item.descrizione} punti={item.punti} />;
      })}
    </div>
  );
};

export default BonusMalusComponent;
