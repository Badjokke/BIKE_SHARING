import React, { useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFlagUsa, faFlag } from '@fortawesome/free-solid-svg-icons';
import i18n from '../../../i18_lan/in18';
import { saveChosenLanguage,loadChosenLanguage } from '../../../token_manager/LocalStorageManager';
const LanguageSwitcher: React.FC = () => {
  const changeLanguage = (language: string) => {
    i18n.changeLanguage(language);
    saveChosenLanguage(language);
  };
  /*
  useEffect(()=>{
      const lang = loadChosenLanguage();
      if(lang)
        changeLanguage(lang);
  },[])
*/

  return (
    <div>
      <button onClick={() => changeLanguage('en')}>
        <FontAwesomeIcon icon={faFlagUsa} />
      </button>
      <button onClick={() => changeLanguage('es')}>
        <FontAwesomeIcon icon={faFlag} />
      </button>
    </div>
  );
};

export default LanguageSwitcher;