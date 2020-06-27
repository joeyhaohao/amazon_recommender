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

import { getRecommendList, getCurrentUser } from "../util/APIUtils";

import RecommendList from "./RecommendationList";
import { useStyles } from "./MyStyle";
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
      currentUser: null,
      recommendList: [],
      guessList: [],
    };
    this.loadRecommendation = this.loadRecommendation.bind(this);
    this.loadGuess = this.loadGuess.bind(this);
    this.loadCurrentUser = this.loadCurrentUser.bind(this);
  }

  async componentDidMount() {
    await this.loadCurrentUser();
    if (this.state.currentUser) {
      this.loadRecommendation();
      this.loadGuess();
    }
  }

  async loadCurrentUser() {
    this.setState({
      isLoading: true,
    });
    await getCurrentUser()
      .then((response) => {
        this.setState({
          currentUser: response,
          isLoading: false,
        });
        console.log("load user success");
      })
      .catch((error) => {
        this.setState({
          isLoading: false,
        });
      });
    console.log(this.state.currentUser);
  }

  loadGuess() {
    this.setState({
      isLoading: true,
    });

    let userId = this.state.currentUser.userId;

    console.log("load guess again!");

    getRecommendList("realtime/" + userId).then(
      (response) => {
        this.setState({
          guessList: response.recList,
          isLoading: false,
        });
        console.log("real time");
      },
      (error) => {
        getRecommendList("top_rate")
          .then((response) => {
            this.setState({
              guessList: response,
              isLoading: false,
            });
            console.log("top rate");
          })
          .catch((err) => {
            this.setState({
              guessList: [],
              isLoading: false,
            });
          });
      }
    );
  }

  loadRecommendation() {
    this.setState({
      isLoading: true,
    });

    let userId = this.state.currentUser.userId;

    getRecommendList("als/" + userId).then(
      (response) => {
        this.setState({
          recommendList: response.recList,
          isLoading: false,
        });
        console.log("als");
      },
      (error) => {
        getRecommendList("trending")
          .then((response) => {
            this.setState({
              recommendList: response,
              isLoading: false,
            });
            console.log("trending");
          })
          .catch((err) => {
            this.setState({
              recommendList: [],
              isLoading: false,
            });
          });
      }
    );
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
            productList={this.state.recommendList}
            loadGuess={this.loadGuess}
            userId={this.state.currentUser ? this.state.currentUser.userId : 0}
          />
          <RecommendList
            currentUser={this.props.currentUser}
            title="Guess you like"
            productList={this.state.guessList}
            loadGuess={this.loadGuess}
            userId={this.state.currentUser ? this.state.currentUser.userId : 0}
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
