import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';
import i18n from './i18_lan/in18';
import { I18nextProvider } from "react-i18next";
const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
console.log("process env: "+process.env.REACT_APP_API_URL);
console.log("process env: "+process.env.REACT_APP_RIDE_SERVICE_URL);
console.log("process env: "+process.env.REACT_APP_USER_SERVICE_URL);

root.render(
    <I18nextProvider i18n={i18n}>
      <App />
    </I18nextProvider>
);
// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
