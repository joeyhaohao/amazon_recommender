import React, { Component } from "react";

import Container from "@material-ui/core/Container";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Rating from "@material-ui/lab/Rating";
import AppBar from "@material-ui/core/AppBar";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import CssBaseline from "@material-ui/core/CssBaseline";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import { Link } from "react-router-dom";
import { withStyles } from "@material-ui/core/styles";

import Product from "../main/Product";
import MyCarousel from "../util/MyCarousel";
import { getProduct, getRecommendList, rateProduct } from "../util/APIUtils";
import { UserContext } from "../auth/UserContext";
import { MAX_NUM_ITEMS_IN_PEOPLE_ALSO_SEE } from "../constants/index";

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
	detailsBox: {
		minHeight: "50vh",
	},
	image: {
		// backgroundImage: "url({https://source.unsplash.com/random})",
		backgroundRepeat: "no-repeat",
		backgroundColor: theme.palette.type === "light" ? theme.palette.grey[50] : theme.palette.grey[900],
		backgroundSize: "cover",
		backgroundPosition: "center",
	},
	items: {
		margin: "8px",
	},

	icon: {
		marginRight: theme.spacing(2),
	},
	title: {
		flexGrow: 1,
	},
});

class ProductView extends Component {
	static contextType = UserContext;

	constructor(props) {
		super(props);
		this.state = {
			isLoading: false,
			// productDetail: {product{id, productId, title, description, categories, imUrl, price},
			// ratingList[{userId, productId, rating, timestamp},{},...],ratingCount, ratingAvg}
			productDetail: null,
			ratingAvg: 0,
			ratingCount: 0,
			peopleAlsoLike: [],
		};
		this.loadProduct = this.loadProduct.bind(this);
		this.loadPeopleAlsoLike = this.loadPeopleAlsoLike.bind(this);
	}

	componentDidMount() {
		// product: {productId, score}
		this.loadProduct();
		this.loadPeopleAlsoLike();
	}

	componentDidUpdate(prevProps) {
		if (prevProps.match.params.id !== this.props.match.params.id) {
			this.loadProduct();
			this.loadPeopleAlsoLike();
		}
	}

	loadProduct() {
		const productId = this.props.match.params.id;
		console.log("product Id is: " + productId);
		this.setState({
			isLoading: true,
		});
		getProduct(productId).then(
			(response) => {
				this.setState({
					productDetail: response,
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

	loadPeopleAlsoLike() {
		const productId = this.props.match.params.id;
		console.log(productId);
		getRecommendList("itemcf/" + productId).then(
			(response) => {
				this.setState({
					peopleAlsoLike: response.recList,
				});
				console.log("itemcf");
			},
			(error) => {
				console.log(error);
			}
		);
	}

	handleRate = (newVal) => {
		const productId = this.props.match.params.id;
		const rateRequest = {
			userId: this.context.userId,
			rate: newVal,
		};
		console.log("Rating!!");
		console.log(rateRequest);
		rateProduct(productId, rateRequest).then(
			(response) => {
				console.log("response!!");
				console.log(response);
				// this.props.loadGuess();
				this.loadProduct();
			},
			(error) => {
				console.log("fail to rate product");
				console.log(error);
			}
		);
	};

	render() {
		const { classes } = this.props;

		return (
			<div>
				{this.state.productDetail ? (
					<div className={classes.root}>
						{/* <AppBar position="relative">
							<Toolbar>
								<ShoppingBasketIcon className={classes.icon} />
								<Typography component={Link} to={"/"} className={classes.title} variant="h6" color="inherit" noWrap>
									<Box>Amazon Recommender</Box>
								</Typography>

								<IconButton onClick={this.props.handleLogout} color="inherit" title="logout">
									<ExitToAppIcon fontSize="large" />
								</IconButton>
							</Toolbar>
						</AppBar> */}

						<Container component="main" className={classes.main} maxWidth="lg">
							<Box m={2} p={2} className={classes.detailsBox}>
								<Typography variant="h2" component="h1" gutterBottom align="center">
									Product Details
								</Typography>

								<Grid container>
									<Grid
										item
										xs={false}
										sm={4}
										md={6}
										lg={6}
										className={classes.image}
										style={{
											backgroundImage: "url(" + this.state.productDetail.imUrl + ")",
										}}
									/>
									<Grid item xs={12} sm={8} md={6} lg={6} component={Paper} elevation={6} square>
										<Box m={2}>
											<Typography variant="h5" gutterBottom className={classes.items}>
												{this.state.productDetail.title}
											</Typography>
											<div className={classes.items}>
												<Rating
													name="simple-controlled"
													precision={0.5}
													value={this.state.ratingAvg}
													onChange={(event, newValue) => {
														event.preventDefault();
														this.handleRate(newValue);
													}}
												/>
												<div>
													<Typography>{this.state.ratingCount} ratings</Typography>
												</div>
											</div>

											<Typography variant="h6" gutterBottom className={classes.items}>
												Categories: {this.state.productDetail.categories}
											</Typography>
											<Typography variant="h6" gutterBottom className={classes.items}>
												Prices: $ {this.state.productDetail.price}
											</Typography>

											<br />
											<br />
											<Typography variant="h5" gutterBottom className={classes.items}>
												Product description:
											</Typography>
											<Typography variant="body1" gutterBottom className={classes.items}>
												{this.state.productDetail.description}
											</Typography>
										</Box>
									</Grid>
								</Grid>
							</Box>

							<Box mt={20}>
								<Typography variant="h2" component="h1" gutterBottom align="center">
									People also like
								</Typography>

								<MyCarousel>
									{this.state.peopleAlsoLike.slice(0, MAX_NUM_ITEMS_IN_PEOPLE_ALSO_SEE).map((product, index) => (
										<Box key={index} component="div" m={2} height="90%">
											<Product
												product={product}
												// loadGuess={this.props.loadGuess}
											/>
										</Box>
									))}
								</MyCarousel>
							</Box>
						</Container>
					</div>
				) : null}
			</div>
		);
	}
}

export default withStyles(useStyles)(ProductView);
