import React, { Component } from "react";

import Grid from "@material-ui/core/Grid";

import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";

import { getRecommendList } from "../util/APIUtils";
import Product from "./Product";

const useStyles = (theme) => ({
  icon: {
    marginRight: theme.spacing(2),
  },
  heroContent: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(8, 0, 6),
  },
  heroButtons: {
    marginTop: theme.spacing(4),
  },
  cardGrid: {
    paddingTop: theme.spacing(8),
    paddingBottom: theme.spacing(8),
  },
  card: {
    height: "100%",
    display: "flex",
    flexDirection: "column",
  },
  cardMedia: {
    paddingTop: "56.25%", // 16:9
  },
  cardContent: {
    flexGrow: 1,
  },
  footer: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(6),
  },
});

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

  loadRecommendation() {
    this.setState({
      isLoading: true,
    });
    getRecommendList().then(
      (response) => {
        this.setState({
          recommendList: response,
          isLoading: false,
        });
        console.log(this.state.recommendList);
      },
      (error) => {
        this.setState({
          isLoading: false,
        });
        console.log(error);
      }
    );
  }

  render() {
    const { classes } = this.props;

    return (
      <React.Fragment>
        {/* Hero unit */}
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
        {/* End hero unit */}
        <Container className={classes.cardGrid} maxWidth="md">
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
