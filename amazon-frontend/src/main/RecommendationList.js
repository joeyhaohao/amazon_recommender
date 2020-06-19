import React, { Component } from "react";

import Grid from "@material-ui/core/Grid";

import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";

import { getRecommendList } from "../util/APIUtils";
import Product from "./Product";
import { useStyles } from "./MyStyle";

class RecommendList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
      recommendList: [],
    };
    this.loadRecommendation = this.loadRecommendation.bind(this);
  }

  componentDidMount() {
    this.loadRecommendation();
  }

  componentDidUpdate(prevProps) {
    if (this.props.currentUser !== prevProps.currentUser) {
      this.loadRecommendation();
    }
  }

  loadRecommendation() {
    this.setState({
      isLoading: true,
    });

    let userId = this.props.currentUser
      ? this.props.currentUser.userId
      : "null";

    console.log(userId);

    if (this.props.title === "Recommend for you") {
      getRecommendList("als/" + userId).then(
        (response) => {
          this.setState({
            recommendList: response.recList,
            isLoading: false,
          });
          // console.log(this.state.recommendList);
        },
        (error) => {
          getRecommendList("trending")
            .then((response) => {
              this.setState({
                recommendList: response,
                isLoading: false,
              });
              // console.log(this.state.recommendList);
            })
            .catch((error) => {
              this.setState({
                recommendList: [],
                isLoading: false,
              });
            });
        }
      );
    } else {
      getRecommendList("realtime/" + userId).then(
        (response) => {
          this.setState({
            recommendList: response.recList,
            isLoading: false,
          });
        },
        (error) => {
          getRecommendList("top_rate")
            .then((response) => {
              this.setState({
                recommendList: response,
                isLoading: false,
              });
            })
            .catch((error) => {
              this.setState({
                recommendList: [],
                isLoading: false,
              });
            });
        }
      );
    }
  }

  render() {
    const { classes } = this.props;

    return (
      <React.Fragment>

        <div className={classes.heroContent}>
          <Container maxWidth="sm">
            <Typography
              component="h1"
              variant="h2"
              align="center"
              color="textPrimary"
              gutterBottom
            >
              {this.props.title}
            </Typography>
          </Container>
        </div>

        <Container className={classes.cardGrid} maxWidth="lg">
          <Grid container spacing={4}>
            {this.state.recommendList.map((product, index) => (
              <Product key={index} product={product} />
            ))}
          </Grid>
        </Container>
      </React.Fragment>
    );
  }
}

export default withStyles(useStyles)(RecommendList);
