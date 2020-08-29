import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import IconButton from "@material-ui/core/IconButton";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import SearchIcon from "@material-ui/icons/Search";
import InputBase from "@material-ui/core/InputBase";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import { fade, makeStyles } from "@material-ui/core/styles";
import { Link } from "react-router-dom";
import { getCurrentUser } from "../util/APIUtils";

const useStyles = makeStyles((theme) => ({
	icon: {
		marginRight: theme.spacing(2),
	},
	title: {
		flexGrow: 1,
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
	searchIcon: {
		padding: theme.spacing(0, 1),
		height: "100%",
		position: "absolute",
		pointerEvents: "auto",
		display: "flex",
		alignItems: "center",
		justifyContent: "center",
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

export default function MyAppBar(props) {
	const appBarTitle = "Amazon Recommender";
	const classes = useStyles();
	const isCancelled = React.useRef(false);
	const [currentUser, setCurrentUser] = React.useState(null);
	const [searchText, setSearchText] = React.useState("");

	const onSearchChange = (event) => {
		setSearchText(event.target.value);
	};

	const handleSearch = (event) => {
		// console.log(searchText);
		props.history.push("/search?q=" + searchText);
		setSearchText("");
	};

	React.useEffect(() => {
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
		};

		loadCurrentUser();

		return () => {
			isCancelled.current = true;
		};
	}, []);

	return (
		<div>
			<AppBar position="fixed">
				<Toolbar>
					<ShoppingBasketIcon className={classes.icon} />
					<Typography component={Link} to={"/"} className={classes.title} variant="h6" color="inherit" noWrap>
						<Box>{appBarTitle}</Box>
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
			</AppBar>
		</div>
	);
}
