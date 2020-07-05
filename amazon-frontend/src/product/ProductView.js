import React, { Component } from "react";

import Container from "@material-ui/core/Container";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Rating from "@material-ui/lab/Rating";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Button from "@material-ui/core/Button";
import { withStyles } from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";
import { getProduct } from "../util/APIUtils";

import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";

import Product from "../main/Product";

const useStyles = (theme) => ({
  root: {
    display: "flex",
    flexDirection: "column",
    minHeight: "100vh",
  },
  main: {
    marginTop: theme.spacing(8),
    marginBottom: theme.spacing(2),
  },
  detailsGrid: {
    minHeight: "50vh",
  },
  image: {
    // backgroundImage: "url({https://source.unsplash.com/random})",
    backgroundRepeat: "no-repeat",
    backgroundColor:
      theme.palette.type === "light"
        ? theme.palette.grey[50]
        : theme.palette.grey[900],
    backgroundSize: "cover",
    backgroundPosition: "center",
  },
  items: {
    margin: "8px",
  },
});

const cards = [1, 2, 3, 4, 5, 6, 7, 8, 9];

const responsive = {
  desktop: {
    breakpoint: { max: 3000, min: 1024 },
    items: 5,
    slidesToSlide: 5, // optional, default to 1.
  },
  tablet: {
    breakpoint: { max: 1024, min: 464 },
    items: 2,
    slidesToSlide: 2, // optional, default to 1.
  },
  mobile: {
    breakpoint: { max: 464, min: 0 },
    items: 1,
    slidesToSlide: 1, // optional, default to 1.
  },
};

class ProductView extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
      // productDetail: {product{id, productId, title, description, categories, imUrl, price},
      // ratingList[{userId, productId, rating, timestamp},{},...],ratingCount, ratingAvg}
      productDetail: null,
      ratingAvg: 0,
      ratingCount: 0,
      peopleAlsoLike:[]
    };
    this.loadProduct = this.loadProduct.bind(this);
  }

  componentDidMount() {
    // product: {productId, score}
    this.loadProduct();
  }

  loadProduct() {
    let productId = this.props.match.params.id;
    this.setState({
      isLoading: true,
    });
    getProduct(productId).then(
      (response) => {
        this.setState({
          productDetail: response.product,
          ratingAvg: response.ratingAvg,
          ratingCount: response.ratingCount,
          isLoading: false,
        });
        console.log(this.state.productDetail);
      },
      (error) => {
        this.setState({
          isLoading: false,
        });
      }
    );
  }

  loadPeopleAlsoLike(){
    let productId = this.props.match.params.id;

    
  }

  render() {
    const { classes } = this.props;

    return (
      <div>
        {this.state.productDetail ? (
          <div className={classes.root}>
            <CssBaseline />
            <Container component="main" className={classes.main} maxWidth="lg">
              <Box m={2} p={2}>
                <Typography
                  variant="h2"
                  component="h1"
                  gutterBottom
                  align="center"
                >
                  Product Details
                </Typography>

                <Grid container className={classes.detailsGrid}>
                  <Grid
                    item
                    xs={false}
                    sm={4}
                    md={6}
                    lg={6}
                    className={classes.image}
                    style={{
                      backgroundImage:
                        "url(" + this.state.productDetail.imUrl + ")",
                    }}
                  />
                  <Grid
                    item
                    xs={12}
                    sm={8}
                    md={6}
                    lg={6}
                    component={Paper}
                    elevation={6}
                    square
                  >
                    <Box m={2}>
                      <Typography
                        variant="h5"
                        gutterBottom
                        className={classes.items}
                      >
                        {this.state.productDetail.title}
                      </Typography>
                      <div className={classes.items}>
                        <Rating
                          name="simple-controlled"
                          precision={0.1}
                          value={this.state.ratingAvg}
                        />
                        <span>
                          <Typography>
                            {this.state.ratingCount} ratings
                          </Typography>
                        </span>
                      </div>

                      <Typography
                        variant="h6"
                        gutterBottom
                        className={classes.items}
                      >
                        Categories: {this.state.productDetail.categories}
                      </Typography>
                      <Typography
                        variant="h6"
                        gutterBottom
                        className={classes.items}
                      >
                        Prices: {this.state.productDetail.price} $
                      </Typography>

                      <br />
                      <br />
                      <Typography
                        variant="h5"
                        gutterBottom
                        className={classes.items}
                      >
                        Product description:
                      </Typography>
                      <Typography
                        variant="body1"
                        gutterBottom
                        className={classes.items}
                      >
                        {this.state.productDetail.description}
                      </Typography>
                    </Box>
                  </Grid>
                </Grid>
              </Box>

              <Box mt={20}>
                <Typography
                  variant="h2"
                  component="h1"
                  gutterBottom
                  align="center"
                >
                  People also like
                </Typography>

                <Carousel
                  swipeable={false}
                  draggable={false}
                  showDots={true}
                  responsive={responsive}
                  // ssr={true} // means to render carousel on server-side.
                  infinite={true}
                  autoPlay={false}
                  // autoPlaySpeed={3000}
                  keyBoardControl={true}
                  // customTransition="all .5"
                  // transitionDuration={500}
                  containerClass="carousel-container"
                  // removeArrowOnDeviceType={["tablet", "mobile"]}
                  // deviceType={this.props.deviceType}
                  dotListClass="custom-dot-list-style"
                  itemClass="carousel-item-padding-40-px"
                >
                  {this.state.peopleAlsoLike.map((product, index) => (
                    <Box key={index} component="div" m={2} height="90%">
                      <Product
                        product={product}
                        // loadGuess={this.props.loadGuess}
                        userId={this.props.userId}
                      />
                    </Box>
                  ))}
                </Carousel>
              </Box>
            </Container>
          </div>
        ) : null}
      </div>
    );
  }
}

export default withStyles(useStyles)(ProductView);
