import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
// Install MaterialUi using npm install @mui/material @emotion/react @emotion/styles
// MaterialUI is designed for usage with Roboto, so install it using npm install @fontsource/roboto
// For icions, install using @mui/icons-material
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';

/*
   List of utiilities and libraries:
    React: https://reactjs.org/
    react-router-dom@6: https://reactrouter.com/web/guides/quick-start
    react-hook-form: https://reacthookform.com/
    @mui/material: https://material-ui.com/
    @emotion/react: https://emotion.sh/docs/react
    @emotion/styles: https://emotion.sh/docs/styles
    @fontsource/roboto: https://fontsource.com/roboto
    @mui/icons-material: https://material-ui.com/icons/
    jsencrypt: https://www.npmjs.com/package/jsencrypt

*/
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
