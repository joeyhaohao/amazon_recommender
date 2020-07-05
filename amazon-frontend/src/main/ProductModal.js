import React from "react";
import { withStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import MuiDialogTitle from "@material-ui/core/DialogTitle";
import MuiDialogContent from "@material-ui/core/DialogContent";
import MuiDialogActions from "@material-ui/core/DialogActions";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";
import Typography from "@material-ui/core/Typography";
import clsx from "clsx";
import Card from "@material-ui/core/Card";
import CardHeader from "@material-ui/core/CardHeader";
import CardMedia from "@material-ui/core/CardMedia";
import CardContent from "@material-ui/core/CardContent";
import CardActions from "@material-ui/core/CardActions";
import Collapse from "@material-ui/core/Collapse";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import Rating from "@material-ui/lab/Rating";
import { makeStyles } from "@material-ui/core/styles";

import { rateProduct } from "../util/APIUtils";

const useStyles = makeStyles((theme) => ({
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
}));

const styles = (theme) => ({
  root: {
    margin: 0,
    padding: theme.spacing(2),
  },
  closeButton: {
    position: "absolute",
    right: theme.spacing(1),
    top: theme.spacing(1),
    color: theme.palette.grey[500],
  },
});

const DialogTitle = withStyles(styles)((props) => {
  const { children, classes, onClose, ...other } = props;
  return (
    <MuiDialogTitle disableTypography className={classes.root} {...other}>
      <Typography variant="h6">{children}</Typography>
      {onClose ? (
        <IconButton
          aria-label="close"
          className={classes.closeButton}
          onClick={onClose}
        >
          <CloseIcon />
        </IconButton>
      ) : null}
    </MuiDialogTitle>
  );
});

const DialogContent = withStyles((theme) => ({
  root: {
    padding: theme.spacing(2),
  },
}))(MuiDialogContent);

const DialogActions = withStyles((theme) => ({
  root: {
    margin: 0,
    padding: theme.spacing(1),
  },
}))(MuiDialogActions);

export default function CustomizedDialogs(props) {
  const classes = useStyles();

  const [open, setOpen] = React.useState(false);
  const [expanded, setExpanded] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  const handleRate = (newVal) => {
    const rateRequest = {
      userId: props.userId,
      rate: newVal,
    };
    console.log("Rating!!");
    console.log(rateRequest);
    rateProduct(props.productDetail.product.productId, rateRequest).then(
      (response) => {
        console.log("response!!");
        console.log(response);
        props.loadGuess();

        props.loadCurrentProduct();
      },
      (error) => {
        console.log("Error!!");
        console.log(error);
      }
    );
  };

  return (
    <div>
      <Button size="small" color="primary" onClick={handleClickOpen}>
        Show details
      </Button>
      <Dialog
        onClose={handleClose}
        aria-labelledby="customized-dialog-title"
        open={open}
      >
        <DialogTitle id="customized-dialog-title" onClose={handleClose}>
          {props.productDetail.product.title}
        </DialogTitle>
        <DialogContent dividers>
          <Card className={classes.root}>
            <CardHeader title={props.productDetail.product.title} />
            <CardMedia
              className={classes.media}
              image={props.productDetail.product.imUrl}
            />
            <CardContent>
              <Typography variant="body2" color="textSecondary" component="p">
                {props.productDetail.product.categories}
              </Typography>

              <Typography>
                Average rating: {props.productDetail.ratingAvg}
              </Typography>
              <Typography>
                Total num of rating: {props.productDetail.ratingCount}
              </Typography>
              <br />
              <Rating
                name="simple-controlled"
                precision={0.1}
                value={props.productDetail.ratingAvg}
                onChange={(event, newValue) => {
                  handleRate(newValue);
                }}
              />
            </CardContent>
            <CardActions disableSpacing>
              {/* <Box component="span" mb={3} borderColor="transparent"> */}

              {/* </Box> */}

              <IconButton
                className={clsx(classes.expand, {
                  [classes.expandOpen]: expanded,
                })}
                onClick={handleExpandClick}
                aria-expanded={expanded}
                aria-label="show more"
              >
                <ExpandMoreIcon />
              </IconButton>
            </CardActions>
            <Collapse in={expanded} timeout="auto" unmountOnExit>
              <CardContent>
                <Typography paragraph>
                  {props.productDetail.product.description}
                </Typography>
              </CardContent>
            </Collapse>
          </Card>
        </DialogContent>
        <DialogActions>
          <Button autoFocus onClick={handleClose} color="primary">
            Save changes
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
