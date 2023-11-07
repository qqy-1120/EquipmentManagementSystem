import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import Login from './login/login'
import reportWebVitals from './reportWebVitals';
import Home from './home/home';
import { Routes,Route, HashRouter } from 'react-router-dom';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <HashRouter>
  <Routes>
    <Route exact path="/" element={<Login/>}/>
    <Route path="/home" element={<Home/>}/>
  </Routes>
  </HashRouter>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
