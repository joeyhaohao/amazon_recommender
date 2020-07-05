import React, { Component } from "react";
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import Container from "@material-ui/core/Container";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import Product from "./Product";
import { useStyles } from "./css/MyStyle";

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

class MyCarousel extends React.PureComponent {
  render() {
    const { classes } = this.props;

    return (
      <React.Fragment>
        <div className={classes.heroContent}>
          <Container maxWidth="lg">
            <Typography
              component="h1"
              variant="h2"
              align="center"
              color="textPrimary"
              gutterBottom
              fontWeight="fontWeightBold"
              fontFamily="italic"
            >
              {this.props.title}
            </Typography>
          </Container>
        </div>
        <Container className={classes.cardGrid} maxWidth="lg">
          {/* <Grid container spacing={4}> */}
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
            {this.props.productList.map((product, index) => (
              <Box key={index} component="div" m={2} height="90%">
                <Product
                  product={product}
                  // loadGuess={this.props.loadGuess}
                  userId={this.props.userId}
                />
              </Box>
            ))}
          </Carousel>
          {/* </Grid> */}
        </Container>
      </React.Fragment>
    );
  }
}

export default withStyles(useStyles)(MyCarousel);
