import React, { Component } from "react";
import { Route, withRouter, Switch } from "react-router-dom";
import CssBaseline from "@material-ui/core/CssBaseline";
import { ACCESS_TOKEN } from "../constants";

import Login from "../auth/Login";
import Registration from "../auth/Registration";
import Album from "../main/Album";
import PrivateRouter from "../util/PrivateRouter";
import ProductView from "../product/ProductView";
import { UserContext } from "../auth/UserContext";
import SearchView from "../search/SearchView";

class App extends Component {
	constructor(props) {
		super(props);
		this.state = {
			userId: "",
		};
	}

	handleLogout = () => {
		localStorage.removeItem(ACCESS_TOKEN);
		this.props.history.push("/");
	};

	setUserId = (newUserId) => {
		this.setState({ userId: newUserId });
	};

	render() {
		const userState = {
			userId: this.state.userId,
			setUserId: this.setUserId,
			logout: this.handleLogout,
		};
		return (
			<div className="App">
				<UserContext.Provider value={userState}>
					<CssBaseline />
					<Switch>
						<Route path="/login" exact component={Login} />
						<Route path="/registration" exact component={Registration} />

						<PrivateRouter path="/" exact component={Album} handleLogout={this.handleLogout} />
						<PrivateRouter path="/product/:id" exact component={ProductView} handleLogout={this.handleLogout} />
						<PrivateRouter path="/search" exact component={SearchView} handleLogout={this.handleLogout} />
					</Switch>
				</UserContext.Provider>
			</div>
		);
	}
}

export default withRouter(App);
