import React, { Component } from "react";
import { Route, withRouter, Switch } from "react-router-dom";

import { ACCESS_TOKEN } from "../constants";

import Login from "../auth/Login";
import Registration from "../auth/Registration";
import Album from "../main/Album";
import PrivateRouter from "../util/PrivateRouter";
import ProductView from "../product/ProductView"

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
    };
  }

  handleLogout = () => {
    localStorage.removeItem(ACCESS_TOKEN);
    this.props.history.push("/");
  };

  render() {
    return (
      <div className="App">
        <Switch>
          <Route path="/login" exact component={Login} />
          <Route path="/registration" exact component={Registration} />

          <PrivateRouter
            path="/"
            exact
            component={Album}
            handleLogout={this.handleLogout}
          />
          <PrivateRouter
            path="/product/:id"
            component={ProductView}
            handleLogout={this.handleLogout}
          />
        </Switch>
      </div>
    );
  }
}

export default withRouter(App);
