import React, { Component } from "react";

import Button from "@material-ui/core/Button";

import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Box from "@material-ui/core/Box";
import Rating from "@material-ui/lab/Rating";
import { withStyles } from "@material-ui/core/styles";

import { Link } from "react-router-dom";

import { getProduct } from "../util/APIUtils";
import { useStyles } from "./css/MyStyle";

class Product extends Component {
	_isMounted = false;
	constructor(props) {
		super(props);
		this.state = {
			isLoading: false,
			// productDetail: {product{id, productId, title, description, categories, imUrl, price},
			// ratingList[{userId, productId, rating, timestamp},{},...],ratingCount, ratingAvg}
			productDetail: null,
			ratingCount: 0,
			ratingAvg: 0,
		};
		this.loadProduct = this.loadProduct.bind(this);
	}

	componentDidMount() {
		this._isMounted = true;
		// product: {productId, score}
		this.loadProduct();
	}

	componentWillUnmount() {
		this._isMounted = false;
	}

	componentDidUpdate(prevProps) {
		if (prevProps.product.productId !== this.props.product.productId) {
			this.loadProduct();
		}
	}

	loadProduct() {
		let productId = this.props.product.productId;
		// console.log(productId);
		this.setState({
			isLoading: true,
		});
		getProduct(productId).then(
			(response) => {
				if (this._isMounted) {
					this.setState({
						productDetail: response.product,
						ratingCount: response.ratingCount,
						ratingAvg: response.ratingAvg,
						isLoading: false,
					});
				}
			},
			(error) => {
				if (this._isMounted) {
					this.setState({
						isLoading: false,
					});
				}
			}
		);
	}

	render() {
		const { classes } = this.props;

		return (
			<React.Fragment>
				<Card className={classes.card}>
					<CardMedia className={classes.cardMedia} image={this.state.productDetail ? this.state.productDetail.imUrl : "null"} />

					<CardContent className={classes.cardContent} style={{ whiteSpace: "nowrap" }}>
						<Box component="div" my={2} textOverflow="ellipsis" overflow="hidden" bgcolor="background.paper">
							{this.state.productDetail ? this.state.productDetail.title : "Product title"}
						</Box>

						<Rating precision={0.5} value={this.state.ratingAvg} readOnly />
					</CardContent>

					<CardActions>
						<Button
							component={Link}
							to={"/product/" + (this.state.productDetail ? this.state.productDetail.productId : null)}
							size="small"
							color="primary"
						>
							Show details
						</Button>
					</CardActions>
				</Card>
			</React.Fragment>
		);
	}
}

export default withStyles(useStyles)(Product);
