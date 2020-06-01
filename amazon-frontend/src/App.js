import React from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import Login from "./auth/Login";
import Registration from "./auth/Registration";
import Album from "./main/Album";
import MainPage from "./main/MainPage";

function App() {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route path="/login" exact component={Login} />
          <Route path="/registration" exact component={Registration} />
          <Route path="/main" exact component={Album} />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
