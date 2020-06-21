import React, { Component } from "react";

import Grid from "@material-ui/core/Grid";

import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";

import Product from "./Product";
import { useStyles } from "./MyStyle";

class RecommendList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
    };
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
            {this.props.productList.map((product, index) => (
              <Product
                key={index}
                product={product}
                loadGuess={this.props.loadGuess}
              />
            ))}
          </Grid>
        </Container>
      </React.Fragment>
    );
  }
}

export default withStyles(useStyles)(RecommendList);
