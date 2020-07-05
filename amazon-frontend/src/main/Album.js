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
import MyCarousel from "./MyCarousel";

import { useStyles } from "./css/MyStyle";
import "./css/Popup.css";

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
      currentUser: null,
      recommendList: null,
      guessList: null,
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
    await getCurrentUser()
      .then((response) => {
        this.setState({
          currentUser: response,
        });
        console.log("load user success");
      })
      .catch((error) => {
        console.log("Cannot load user");
      });
    console.log(this.state.currentUser);
  }

  loadGuess() {


    let userId = this.state.currentUser.userId;

    getRecommendList("realtime/" + userId).then(
      (response) => {
        this.setState({
          guessList: response.recList,
        });
        console.log("realtime");
      },
      (error) => {
        getRecommendList("top_rate")
          .then((response) => {
            this.setState({
              guessList: response,
            });
            console.log("top rate");
          })
          .catch((err) => {
            this.setState({
              guessList: [],
            });
          });
      }
    );
  }

  loadRecommendation() {
    let userId = this.state.currentUser.userId;

    getRecommendList("als/" + userId).then(
      (response) => {
        this.setState({
          recommendList: response.recList,
        });
        console.log("als");
      },
      (error) => {
        getRecommendList("trending")
          .then((response) => {
            this.setState({
              recommendList: response,
            });
            console.log("trending");
          })
          .catch((err) => {
            this.setState({
              recommendList: [],
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
          {this.state.recommendList && this.state.guessList ? (
            <div>
              <MyCarousel
                title="Recommend for you"
                productList={this.state.recommendList}
                // loadGuess={this.loadGuess}
                userId={this.state.currentUser.userId}
              />
              <MyCarousel
                title="Guess you like"
                productList={this.state.guessList}
                // loadGuess={this.loadGuess}
                userId={this.state.currentUser.userId}
              />
              {/* <RecommendList
                currentUser={this.props.currentUser}
                title="Recommend for you"
                productList={this.state.recommendList}
                loadGuess={this.loadGuess}
                userId={
                  this.state.currentUser ? this.state.currentUser.userId : 0
                }
              />
              <RecommendList
                currentUser={this.props.currentUser}
                title="Guess you like"
                productList={this.state.guessList}
                loadGuess={this.loadGuess}
                userId={
                  this.state.currentUser ? this.state.currentUser.userId : 0
                }
              /> */}
            </div>
          ) : null}
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
