import React, { Component } from "react";

import Button from "@material-ui/core/Button";
import AppBar from "@material-ui/core/AppBar";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import CssBaseline from "@material-ui/core/CssBaseline";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import IconButton from "@material-ui/core/IconButton";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import { withStyles } from "@material-ui/core/styles";

import { Link } from "react-router-dom";

import { getRecommendList, getCurrentUser } from "../util/APIUtils";
import CarouselView from "./CarouselView";
import { useStyles } from "./css/MyStyle";
import { UserContext } from "../auth/UserContext";

class Album extends Component {
	static contextType = UserContext;
	_isMounted = false;

	constructor(props) {
		super(props);
		this.state = {
			title: "Amazon Recommender",
			currentUser: null,
			recommendList: null,
			guessList: null,
		};
		this.loadRecommendation = this.loadRecommendation.bind(this);
		this.loadGuess = this.loadGuess.bind(this);
		this.loadCurrentUser = this.loadCurrentUser.bind(this);
	}

	async componentDidMount() {
		this._isMounted = true;
		await this.loadCurrentUser();
		this.context.setUserId(this.state.currentUser.userId);
		console.log("Context: " + this.context.userId);
		if (this.state.currentUser) {
			this.loadRecommendation();
			this.loadGuess();
		}
	}

	componentWillUnmount() {
		this._isMounted = false;
	}

	async loadCurrentUser() {
		await getCurrentUser()
			.then((response) => {
				if (this._isMounted) {
					this.setState({
						currentUser: response,
					});
				}
				console.log("load user success");
			})
			.catch((error) => {
				console.log(error);
				console.log("Cannot load user");
			});
		console.log(this.state.currentUser);
	}

	loadGuess() {
		let userId = this.state.currentUser.userId;

		getRecommendList("realtime/" + userId).then(
			(response) => {
				if (this._isMounted) {
					this.setState({
						guessList: response.recList,
					});
				}
				console.log("realtime");
			},
			(error) => {
				getRecommendList("top_rate")
					.then((response) => {
						if (this._isMounted) {
							this.setState({
								guessList: response,
							});
						}
						console.log("top rate");
					})
					.catch((err) => {
						if (this._isMounted) {
							this.setState({
								guessList: [],
							});
						}
					});
			}
		);
	}

	loadRecommendation() {
		let userId = this.state.currentUser.userId;

		getRecommendList("als/" + userId).then(
			(response) => {
				if (this._isMounted) {
					this.setState({
						recommendList: response.recList,
					});
				}
				console.log("als");
			},
			(error) => {
				getRecommendList("trending")
					.then((response) => {
						if (this._isMounted) {
							this.setState({
								recommendList: response,
							});
						}
						console.log("trending");
					})
					.catch((err) => {
						if (this._isMounted) {
							this.setState({
								recommendList: [],
							});
						}
					});
			}
		);
	}

	render() {
		const { classes } = this.props;

		return (
			<React.Fragment>
				<CssBaseline />

				<AppBar position="relative">
					<Toolbar>
						<ShoppingBasketIcon className={classes.icon} />
						<Typography className={classes.title} variant="h6" color="inherit" noWrap>
							{this.state.title}
						</Typography>
						{this.state.currentUser ? (
							<Typography variant="h6" color="inherit">
								Hello {this.state.currentUser.username}!
							</Typography>
						) : (
							<Button component={Link} to="/login" color="inherit">
								Login
							</Button>
						)}
						<IconButton onClick={this.props.handleLogout} color="inherit" title="logout">
							<ExitToAppIcon fontSize="large" />
						</IconButton>
						{/* <Button color="inherit" onClick={this.props.handleLogout}>
							Log out
						</Button> */}
					</Toolbar>
				</AppBar>

				<main className={classes.main}>
					{this.state.recommendList && this.state.guessList ? (
						<div>
							<CarouselView
								title="Recommend for you"
								productList={this.state.recommendList}
								// loadGuess={this.loadGuess}
								userId={this.state.currentUser.userId}
							/>
							<CarouselView
								title="Guess you like"
								productList={this.state.guessList}
								// loadGuess={this.loadGuess}
								userId={this.state.currentUser.userId}
							/>
						</div>
					) : null}
				</main>
			</React.Fragment>
		);
	}
}

export default withStyles(useStyles)(Album);
