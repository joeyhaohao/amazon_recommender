import React, { Component } from "react";
import { Route, withRouter, Switch, Redirect } from "react-router-dom";

import { getCurrentUser } from "../util/APIUtils";
import { ACCESS_TOKEN } from "../constants";

import Login from "../auth/Login";
import Registration from "../auth/Registration";
import Album from "../main/Album";
import PrivateRouter from "../util/PrivateRouter";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentUser: null,
      isAuthenticated: false,
      isLoading: false,
    };
    this.handleLogout = this.handleLogout.bind(this);
    this.loadCurrentUser = this.loadCurrentUser.bind(this);
    this.handleLogin = this.handleLogin.bind(this);
  }

  loadCurrentUser() {
    this.setState({
      isLoading: true,
    });
    getCurrentUser()
      .then((response) => {
        this.setState({
          currentUser: response,
          isAuthenticated: true,
          isLoading: false,
        });
      })
      .catch((error) => {
        this.setState({
          isLoading: false,
        });
      });
  }

  componentDidMount() {
    this.loadCurrentUser();
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
    this.loadCurrentUser();
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
            currentUser={this.state.currentUser}
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
