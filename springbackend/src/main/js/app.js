// 'use strict';


import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

// import './index.css'
import Form from './Form'
import Login from './Login'
import Registration from "./Registration";

const inputs = [{
    name: "username",
    placeholder: "username",
    type: "text"
}, {
    name: "password",
    placeholder: "password",
    type: "password"
}, {
    type: "submit",
    value: "Submit",
    className: "btn"
}]

const props = {
    name: 'loginForm',
    method: 'POST',
    action: '/perform_login',
    inputs: inputs
}

const params = new URLSearchParams(window.location.search)

ReactDOM.render(
    // <Router>
    //     <Switch>
    //         <Route path="/" exact component={Login} />
    //         <Route path="/registration" exact component={Registration} />
    //     </Switch>
    // </Router>,
    <Login/>,
    document.getElementById('container'))

