import React, { Component } from "react";

import Button from "@material-ui/core/Button";

import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Typography from "@material-ui/core/Typography";

import Grid from "@material-ui/core/Grid";

import { withStyles } from "@material-ui/core/styles";
import { getProduct } from "../util/APIUtils";
import { useStyles } from "./MyStyle";
import ProductDetail from "./ProductDetail";

class Product extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
      productDetail: null,
    };
    this.loadProduct = this.loadProduct.bind(this);
  }

  componentDidMount() {
    this.loadProduct(this.props.product.productId);
  }

  loadProduct(productId) {
    getProduct(productId).then(
      (response) => {
        this.setState({
          productDetail: response.product,
          isLoading: false,
          popup: false,
        });
        // console.log(this.state.productDetail);
      },
      (error) => {
        this.setState({
          isLoading: false,
        });
        console.log(error);
      }
    );
  }

  // This syntax ensures `this` is bound within
  togglePop = () => {
    this.setState({
      popup: !this.state.popup,
    });
  };

  render() {
    const { classes } = this.props;

    return (
      <React.Fragment>
        <Grid item xs={12} sm={6} md={4}>
          <Card className={classes.card}>
            <CardMedia
              className={classes.cardMedia}
              image={
                this.state.productDetail
                  ? this.state.productDetail.imUrl
                  : "https://source.unsplash.com/random"
              }
              title="Image title"
            />
            <CardContent className={classes.cardContent}>
              <Typography gutterBottom variant="h5" component="h2">
                {this.state.productDetail
                  ? this.state.productDetail.title
                  : "Product title"}
              </Typography>
              <Typography>
                {/* {this.state.productDetail
                  ? this.state.productDetail.description
                  : "Product description"} */}
                This is a media card. You can use this section to describe the
                content.
              </Typography>
            </CardContent>
            <CardActions>
              <Button size="small" color="primary" onClick={this.togglePop}>
                View
              </Button>
              {this.state.popup ? (
                <ProductDetail
                  toggle={this.togglePop}
                  product={this.state.productDetail}
                />
              ) : null}
            </CardActions>
          </Card>
        </Grid>
      </React.Fragment>
    );
  }
}

export default withStyles(useStyles)(Product);
