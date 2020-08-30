import React from "react";
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
import { fade, makeStyles } from "@material-ui/core/styles";
import { Link } from "react-router-dom";

import { getCurrentUser, searchProducts } from "../util/APIUtils";
import CarouselView from "../main/CarouselView";

const queryString = require("query-string");

const useStyles = makeStyles((theme) => ({
	icon: {
		marginRight: theme.spacing(2),
	},
	title: {
		flexGrow: 1,
	},
	heroContent: {
		// backgroundColor: theme.palette.background.paper,
		padding: theme.spacing(3, 0, 2),
		borderColor: "black",
	},
	main: {
		// backgroundColor: "white",
		paddingTop: "60px",
		minHeight: "100vh",
	},
	mainContainer: {
		backgroundColor: theme.palette.background.paper,
	},
	search: {
		position: "relative",
		borderRadius: theme.shape.borderRadius,
		backgroundColor: fade(theme.palette.common.white, 0.15),
		"&:hover": {
			backgroundColor: fade(theme.palette.common.white, 0.25),
		},
		marginRight: theme.spacing(1),
		marginLeft: 0,
		width: "100%",
		[theme.breakpoints.up("sm")]: {
			marginLeft: theme.spacing(3),
			width: "auto",
		},
	},
	inputRoot: {
		color: "inherit",
	},
	inputInput: {
		padding: theme.spacing(1, 1, 1, 0),
		// vertical padding + font size from searchIcon
		paddingLeft: `calc(1em + ${theme.spacing(0)}px)`,
		transition: theme.transitions.create("width"),
		width: "100%",
		[theme.breakpoints.up("md")]: {
			width: "20ch",
		},
	},
}));

export default function SearchView(props) {
	const classes = useStyles();
	const isCancelled = React.useRef(false);
	const [currentUser, setCurrentUser] = React.useState(null);
	const [searchText, setSearchText] = React.useState("");
	const [resultList, setResultList] = React.useState([]);

	const onSearchChange = (event) => {
		setSearchText(event.target.value);
	};

	const handleSearch = (event) => {
		// console.log(searchText);
		props.history.push("/search?q=" + searchText);
		setSearchText("");
	};

	React.useEffect(() => {
		const searchResult = async (searchRequest) => {
			await searchProducts(searchRequest).then((res) => {
				setResultList(res);
				console.log(resultList);
			});
		};

		const loadCurrentUser = async () => {
			await getCurrentUser()
				.then((response) => {
					if (!isCancelled.current) {
						setCurrentUser(response);
					}
					console.log(response);
				})
				.catch((error) => {
					console.log(error);
				});
			console.log(currentUser);
			if (currentUser) {
				const parsed = queryString.parse(props.location.search);
				console.log(parsed);
				const q = parsed.q;
				const searchRequest = {
					userId: currentUser.userId,
					keyword: q,
					page: 1,
				};
				await searchResult(searchRequest);
			}
		};

		loadCurrentUser();

		return () => {
			isCancelled.current = true;
		};
	}, [currentUser,props.location.search,isCancelled.current]);

	return (
		<div>
			{/* <AppBar position="fixed">
				<Toolbar>
					<ShoppingBasketIcon className={classes.icon} />
					<Typography component={Link} to={"/"} className={classes.title} variant="h6" color="inherit" noWrap>
						<Box>Amazon Recommender</Box>
					</Typography>
					<div className={classes.search}>
						<InputBase
							placeholder="Search…"
							classes={{
								root: classes.inputRoot,
								input: classes.inputInput,
							}}
							inputProps={{ "aria-label": "search" }}
							onChange={onSearchChange}
							value={searchText}
						/>
					</div>
					<IconButton onClick={handleSearch} color="inherit">
						<SearchIcon fontSize="large" />
					</IconButton>
					{currentUser ? (
						<Typography variant="h6" color="inherit">
							Hello {currentUser.username}!
						</Typography>
					) : (
						<Button component={Link} to="/login" color="inherit">
							Login
						</Button>
					)}

					<IconButton onClick={props.handleLogout} color="inherit" title="logout">
						<ExitToAppIcon fontSize="large" />
					</IconButton>
				</Toolbar>
			</AppBar> */}

			<main className={classes.main}>
				{currentUser && resultList ? (
					<div>
						<Container maxWidth="lg" className={classes.mainContainer}>
							<CarouselView title="Search Result" productList={resultList} userId={currentUser.userId} />
						</Container>
					</div>
				) : null}
			</main>
		</div>
	);
}
