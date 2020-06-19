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
import FavoriteIcon from "@material-ui/icons/Favorite";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import CloseIcon from "@material-ui/icons/Close";
import Rating from "@material-ui/lab/Rating";
import Box from "@material-ui/core/Box";

import { rateProduct } from "../util/APIUtils";

const useStyles = (theme) => ({
  root: {
    // maxWidth: "80%",
    marginLeft: "auto",
  },
  media: {
    height: 0,
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
      rateVal: 0,
    };
  }

  handleRate = () => {
    const rateRequest = {
      userId: "001",
      rate: 5.0,
    };
    rateProduct(this.props.product.productId, rateRequest).then((response) => {
      console.log(response);
    });
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
              title={
                this.props.product ? this.props.product.product.title : "Title"
              }
            />
            <CardMedia
              className={classes.media}
              image={
                this.props.product
                  ? this.props.product.product.imUrl
                  : "https://source.unsplash.com/random"
              }
            />
            <CardContent>
              <Typography variant="body2" color="textSecondary" component="p">
                {this.props.product
                  ? this.props.product.product.categories
                  : "Product score"}
              </Typography>

              <Typography>
                Average rating: {this.props.product.ratingAvg}
              </Typography>
              <Typography>
                Total num of rating: {this.props.product.ratingCount}
              </Typography>
              <br/>
              <Rating
                  name="simple-controlled"
                  value={this.state.rateVal}
                  onChange={(event, newValue) => {
                    this.setValue({ rateVal: newValue });
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
                  {this.props.product
                    ? this.props.product.product.description
                    : "Heat 1/2 cup of the broth in a pot until simmering, add saffron and set aside for 10 minutes."}
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
