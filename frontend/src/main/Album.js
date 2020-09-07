import React, { Component } from "react";

import Button from "@material-ui/core/Button";
import AppBar from "@material-ui/core/AppBar";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import Box from "@material-ui/core/Box";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import IconButton from "@material-ui/core/IconButton";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import SearchIcon from "@material-ui/icons/Search";
import InputBase from "@material-ui/core/InputBase";
import Container from "@material-ui/core/Container";
import { withStyles } from "@material-ui/core/styles";

import { Link } from "react-router-dom";

import { getRecommendList, getCurrentUser } from "../util/APIUtils";
import CarouselView from "./CarouselView";
import { useStyles } from "./css/MyStyle";
import { UserContext } from "../auth/UserContext";
import { MAX_NUM_ITEMS_IN_CAROUSEL } from "../constants/index";

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
			searchText: "",
		};

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

	loadGuess = () => {
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
	};

	loadRecommendation = () => {
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
	};

	onSearchChange = (event) => {
		this.setState({ searchText: event.target.value });
	};

	handleSearch = () => {
		this.props.history.push("/search?q=" + this.state.searchText);
		this.setState({ searchText: "" });
	};

	render() {
		const { classes } = this.props;

		return (
			<div>
				{/* <AppBar position="fixed">
					<Toolbar>
						<ShoppingBasketIcon className={classes.icon} />
						<Typography component={Link} to={"/"} className={classes.title} variant="h6" color="inherit" noWrap>
							<Box>{this.state.title}</Box>
						</Typography>
						<div className={classes.search}>
							<InputBase
								placeholder="Search…"
								classes={{
									root: classes.inputRoot,
									input: classes.inputInput,
								}}
								inputProps={{ "aria-label": "search" }}
								onChange={this.onSearchChange}
								value={this.state.searchText}
							/>
						</div>
						<IconButton onClick={this.handleSearch} color="inherit">
							<SearchIcon fontSize="large" />
						</IconButton>
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
					</Toolbar>
				</AppBar> */}

				<main className={classes.main}>
					{this.state.recommendList && this.state.guessList ? (
						<div>
							<Container maxWidth="lg" className={classes.mainContainer}>
								<CarouselView
									title="Recommend For You"
									productList={this.state.recommendList.slice(0, MAX_NUM_ITEMS_IN_CAROUSEL)}
									// loadGuess={this.loadGuess}
									userId={this.state.currentUser.userId}
								/>
								<CarouselView
									title="Guess You Like"
									productList={this.state.guessList.slice(0, MAX_NUM_ITEMS_IN_CAROUSEL)}
									// loadGuess={this.loadGuess}
									userId={this.state.currentUser.userId}
								/>
							</Container>
						</div>
					) : null}
				</main>
			</div>
		);
	}
}

export default withStyles(useStyles)(Album);
