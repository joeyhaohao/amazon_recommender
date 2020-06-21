import React, { Component } from "react";
import { Route, withRouter, Switch } from "react-router-dom";


import { ACCESS_TOKEN } from "../constants";

import Login from "../auth/Login";
import Registration from "../auth/Registration";
import Album from "../main/Album";
import PrivateRouter from "../util/PrivateRouter";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
    };
    this.handleLogout = this.handleLogout.bind(this);
    this.handleLogin = this.handleLogin.bind(this);
  }

  handleLogout(
    redirectTo = "/",
    notificationType = "success",
    description = "You're successfully logged out."
  ) {
    localStorage.removeItem(ACCESS_TOKEN);

    this.setState({
      currentUser: null,
      isAuthenticated: false,
    });

    this.props.history.push(redirectTo);
  }

  handleLogin() {
    // this.loadCurrentUser();
    this.props.history.push("/");
  }

  render() {
    return (
      <div className="App">
        <Switch>
          <Route
            path="/login"
            exact
            render={(props) => <Login onLogin={this.handleLogin} {...props} />}
          />
          <Route path="/registration" exact component={Registration} />
          {/* <Route exact path="/">
            {this.state.isAuthenticated ? <Redirect to="/login" /> : <Album />}
          </Route> */}
          <PrivateRouter
            path="/"
            exact
            component={Album}
            handleLogout={this.handleLogout}
          />
          {/* <Route
            path="/"
            exact
            render={(props) => (
              <Album
                isAuthenticated={this.state.isAuthenticated}
                currentUser={this.state.currentUser}
                handleLogout={this.handleLogout}
                {...props}
              />
            )}
          /> */}
        </Switch>
      </div>
    );
  }
}

export default withRouter(App);
