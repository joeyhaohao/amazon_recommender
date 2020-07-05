import React, { Component } from 'react';

import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Rating from '@material-ui/lab/Rating';

import { withStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import { getProduct } from '../util/APIUtils';

import Product from '../main/Product';
import MyCarousel from '../util/MyCarousel';
import { getRecommendList } from '../util/APIUtils';

const useStyles = (theme) => ({
	root: {
		display: 'flex',
		flexDirection: 'column',
		minHeight: '100vh',
	},
	main: {
		marginTop: theme.spacing(8),
		marginBottom: theme.spacing(2),
	},
	detailsGrid: {
		minHeight: '50vh',
	},
	image: {
		// backgroundImage: "url({https://source.unsplash.com/random})",
		backgroundRepeat: 'no-repeat',
		backgroundColor: theme.palette.type === 'light' ? theme.palette.grey[50] : theme.palette.grey[900],
		backgroundSize: 'cover',
		backgroundPosition: 'center',
	},
	items: {
		margin: '8px',
	},
});

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
			peopleAlsoLike: [],
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

	loadPeopleAlsoLike() {
		let productId = this.props.match.params.id;

		getRecommendList('top_rate').then(
			(response) => {
				this.setState({
					peopleAlsoLike: response.recList,
				});
				console.log('itemcf');
			},
			(error) => {}
		);
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
								<Typography variant="h2" component="h1" gutterBottom align="center">
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
											backgroundImage: 'url(' + this.state.productDetail.imUrl + ')',
										}}
									/>
									<Grid item xs={12} sm={8} md={6} lg={6} component={Paper} elevation={6} square>
										<Box m={2}>
											<Typography variant="h5" gutterBottom className={classes.items}>
												{this.state.productDetail.title}
											</Typography>
											<div className={classes.items}>
												<Rating name="simple-controlled" precision={0.1} value={this.state.ratingAvg} />
												<div>
													<Typography>{this.state.ratingCount} ratings</Typography>
												</div>
											</div>

											<Typography variant="h6" gutterBottom className={classes.items}>
												Categories: {this.state.productDetail.categories}
											</Typography>
											<Typography variant="h6" gutterBottom className={classes.items}>
												Prices: {this.state.productDetail.price} $
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
									{this.state.peopleAlsoLike.map((product, index) => (
										<Box key={index} component="div" m={2} height="90%">
											<Product
												product={product}
												// loadGuess={this.props.loadGuess}
												userId={this.props.userId}
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
