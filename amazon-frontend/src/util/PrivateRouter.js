import React from "react";
import { Route, Redirect } from "react-router-dom";
import { isLogin } from "../util/APIUtils";

const PrivateRoute = ({
  component: Component,
  isAuthenticated,
  currentUser,
  handleLogout,
  ...rest
}) => {
  return (
    // Show the component only when the user is logged in
    // Otherwise, redirect the user to /login page
    <Route
      {...rest}
      render={(props) =>
        isLogin() ? (
          <Component
            {...props}
            isAuthenticated={isAuthenticated}
            currentUser={currentUser}
            handleLogout={handleLogout}
          />
        ) : (
          <Redirect to="/login" />
        )
      }
    />
  );
};

export default PrivateRoute;
