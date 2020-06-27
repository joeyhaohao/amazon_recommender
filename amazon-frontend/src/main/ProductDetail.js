import React, { Component } from "react";

import { withStyles } from "@material-ui/core/styles";
import clsx from "clsx";
import Card from "@material-ui/core/Card";
import CardHeader from "@material-ui/core/CardHeader";
import CardMedia from "@material-ui/core/CardMedia";
import CardContent from "@material-ui/core/CardContent";
import CardActions from "@material-ui/core/CardActions";
import Collapse from "@material-ui/core/Collapse";

import IconButton from "@material-ui/core/IconButton";
import Typography from "@material-ui/core/Typography";
import { red } from "@material-ui/core/colors";

import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import CloseIcon from "@material-ui/icons/Close";
import Rating from "@material-ui/lab/Rating";

import { rateProduct } from "../util/APIUtils";

const useStyles = (theme) => ({
  root: {
    // maxWidth: "80%",
    marginLeft: "auto",
  },
  media: {
    // height: 0,
    paddingTop: "56.25%", // 16:9,
    marginTop: "30",
  },
  expand: {
    transform: "rotate(0deg)",
    marginLeft: "auto",
    transition: theme.transitions.create("transform", {
      duration: theme.transitions.duration.shortest,
    }),
  },
  expandOpen: {
    transform: "rotate(180deg)",
  },
  avatar: {
    backgroundColor: red[500],
  },
});

class ProductDetail extends Component {
  constructor(props) {
    super(props);
    this.state = {
      expanded: false,
      recommendList: [],
    };
  }

  handleRate = (newVal) => {
    const rateRequest = {
      userId: this.props.userId,
      rate: newVal,
    };
    console.log("Rating!!");
    console.log(rateRequest);
    rateProduct(this.props.productDetail.product.productId, rateRequest).then(
      (response) => {
        console.log("response!!");
        console.log(response);
        this.props.loadGuess();
        if (this.props.listTitle === "Recommend for you") {
          this.props.loadCurrentProduct();
          console.log("Reload this product");
        }
      },
      (error) => {
        console.log("Error!!");
        console.log(error);
      }
    );
  };

  handleExpandClick = () => {
    this.setState({ expanded: !this.state.expanded });
  };

  handleClick = () => {
    this.props.toggle();
  };

  render() {
    const { classes } = this.props;

    return (
      <div className="modal">
        <div className="modal_content">
          <Card className={classes.root}>
            <CardHeader
              action={
                <IconButton aria-label="settings" onClick={this.handleClick}>
                  <CloseIcon />
                </IconButton>
              }
              title={this.props.productDetail.product.title}
            />
            <CardMedia
              className={classes.media}
              image={this.props.productDetail.product.imUrl}
            />
            <CardContent>
              <Typography variant="body2" color="textSecondary" component="p">
                {this.props.productDetail.product.categories}
              </Typography>

              <Typography>
                Average rating: {this.props.productDetail.ratingAvg}
              </Typography>
              <Typography>
                Total num of rating: {this.props.productDetail.ratingCount}
              </Typography>
              <br />
              <Rating
                name="simple-controlled"
                precision={0.1}
                value={this.props.productDetail.ratingAvg}
                onChange={(event, newValue) => {
                  this.handleRate(newValue);
                }}
              />
            </CardContent>
            <CardActions disableSpacing>
              {/* <Box component="span" mb={3} borderColor="transparent"> */}

              {/* </Box> */}

              <IconButton
                className={clsx(classes.expand, {
                  [classes.expandOpen]: this.state.expanded,
                })}
                onClick={this.handleExpandClick}
                aria-expanded={this.state.expanded}
                aria-label="show more"
              >
                <ExpandMoreIcon />
              </IconButton>
            </CardActions>
            <Collapse in={this.state.expanded} timeout="auto" unmountOnExit>
              <CardContent>
                <Typography paragraph>
                  {this.props.productDetail.product.description}
                </Typography>
              </CardContent>
            </Collapse>
          </Card>
        </div>
      </div>
    );
  }
}

export default withStyles(useStyles)(ProductDetail);
