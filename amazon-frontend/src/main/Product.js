import React, { Component } from "react";

import Button from "@material-ui/core/Button";

import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Typography from "@material-ui/core/Typography";

import Rating from "@material-ui/lab/Rating";

import { withStyles } from "@material-ui/core/styles";

import { getProduct } from "../util/APIUtils";
import { useStyles } from "./MyStyle";
import ProductDetail from "./ProductDetail";

class Product extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
      // productDetail: {product{id, productId, title, description, categories, imUrl, price},
      // ratingList[{userId, productId, rating, timestamp},{},...],ratingCount, ratingAvg}
      productDetail: null,
      popup: false,
    };
    this.loadProduct = this.loadProduct.bind(this);
  }

  componentDidMount() {
    // product: {productId, score}
    this.loadProduct();
  }

  loadProduct() {
    let productId = this.props.product.productId;
    this.setState({
      isLoading: true,
    });
    getProduct(productId).then(
      (response) => {
        this.setState({
          productDetail: response,
          isLoading: false,
        });
      },
      (error) => {
        this.setState({
          isLoading: false,
        });
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

          <Card className={classes.card}>
            <CardMedia
              className={classes.cardMedia}
              image={
                this.state.productDetail
                  ? this.state.productDetail.product.imUrl
                  : "null"
              }
              title="Image title"
            />

            <CardContent className={classes.cardContent}>
              <Typography gutterBottom variant="h5" component="h2">
                {this.state.productDetail
                  ? this.state.productDetail.product.title
                  : "Product title"}
              </Typography>

              <Rating
                precision={0.1}
                value={
                  this.state.productDetail
                    ? this.state.productDetail.ratingAvg
                    : null
                }
                name="disabled"
                disabled
              />
            </CardContent>
            <CardActions>
              <Button size="small" color="primary" onClick={this.togglePop}>
                View
              </Button>

              {this.state.popup && this.state.productDetail ? (
                <ProductDetail
                  toggle={this.togglePop}
                  productDetail={this.state.productDetail}
                  loadGuess={this.props.loadGuess}
                  loadCurrentProduct={this.loadProduct}
                  userId={this.props.userId}
                  listTitle={this.props.listTitle}
                />
              ) : null}
            </CardActions>
          </Card>

      </React.Fragment>
    );
  }
}

export default withStyles(useStyles)(Product);
