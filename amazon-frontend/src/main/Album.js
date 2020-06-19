import React, { Component } from "react";

import Button from "@material-ui/core/Button";
import AppBar from "@material-ui/core/AppBar";
import CameraIcon from "@material-ui/icons/PhotoCamera";
import CssBaseline from "@material-ui/core/CssBaseline";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";
import LinkMaterial from "@material-ui/core/Link";

import { Link } from "react-router-dom";

import RecommendList from "./RecommendationList";
import { useStyles } from "./MyStyle";
import ProductDetail from "./ProductDetail";
import "./Popup.css";

function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {"Copyright © "}
      <LinkMaterial color="inherit" href="https://material-ui.com/">
        Your Website
      </LinkMaterial>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

class Album extends Component {
  constructor(props) {
    super(props);
    this.state = {
      title: "Amazon Recommender",
      isLoading: false,
    };
  }

  render() {
    const { classes } = this.props;

    return (
      <React.Fragment>
        <CssBaseline />

        <AppBar position="relative">
          <Toolbar>
            <CameraIcon className={classes.icon} />
            <Typography
              className={classes.title}
              variant="h6"
              color="inherit"
              noWrap
            >
              {this.state.title}
            </Typography>
            <Button component={Link} to="/login" color="inherit">
              Login
            </Button>
            <Button color="inherit" onClick={this.props.handleLogout}>
              Log out
            </Button>
          </Toolbar>
        </AppBar>

        <main>
          <RecommendList
            currentUser={this.props.currentUser}
            title="Recommend for you"
            toggle={this.togglePop}
          />
          <RecommendList
            currentUser={this.props.currentUser}
            title="Guess you like"
          />
        </main>
        {/* Footer */}
        <footer className={classes.footer}>
          <Typography variant="h6" align="center" gutterBottom>
            Footer
          </Typography>
          <Typography
            variant="subtitle1"
            align="center"
            color="textSecondary"
            component="p"
          >
            Something here to give the footer a purpose!
          </Typography>
          <Copyright />
        </footer>
        {/* End footer */}
      </React.Fragment>
    );
  }
}

export default withStyles(useStyles)(Album);
