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
	main: {
		// backgroundColor: "white",
		paddingTop: "60px",
		minHeight: "100vh",
	},
	mainContainer: {
		backgroundColor: theme.palette.background.paper,
	},
	card: {
		height: "100%",
		display: "flex",
		flexDirection: "column",
	},
	cardMedia: {
		paddingTop: "56.25%", // 16:9
	},
	cardContent: {
		flexGrow: 1,
	},
}));

export default function SearchView(props) {
	const classes = useStyles();
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
		let isCancelled = false;
		const searchResult = async (searchRequest) => {
			await searchProducts(searchRequest).then((res) => {
				if (!isCancelled) {
					setResultList(res);
					console.log(res);
				}
			});
		};

		if (currentUser) {
			const parsed = queryString.parse(props.location.search);
			const q = parsed.q;
			const searchRequest = {
				userId: currentUser.userId,
				keyword: q,
				page: 1,
			};
			searchResult(searchRequest);
		}
	}, [currentUser, props.location.search]);

	React.useEffect(() => {
		let isCancelled = false;
		const loadCurrentUser = async () => {
			await getCurrentUser()
				.then((response) => {
					if (!isCancelled) {
						setCurrentUser(response);
					}
				})
				.catch((error) => {
					console.log(error);
				});
		};
		loadCurrentUser();
		return () => {
			isCancelled = true;
		};
	}, []);

	return (
		<div>
			<main className={classes.main}>
				{currentUser && resultList ? (
					<div>
						<Container maxWidth="lg" className={classes.mainContainer}>
							<CarouselView title="Search Result" productList={resultList} userId={currentUser.userId} />
							{/* <Grid container spacing={4}>
								{cards.map((card) => (
									<Grid item key={card} xs={12} sm={6} md={4}>
										<Card className={classes.card}>
											<CardMedia className={classes.cardMedia} image="https://source.unsplash.com/random" title="Image title" />
											<CardContent className={classes.cardContent}>
												<Typography gutterBottom variant="h5" component="h2">
													Heading
												</Typography>
												<Typography>This is a media card. You can use this section to describe the content.</Typography>
											</CardContent>
											<CardActions>
												<Button size="small" color="primary">
													View
												</Button>
												<Button size="small" color="primary">
													Edit
												</Button>
											</CardActions>
										</Card>
									</Grid>
								))}
							</Grid>*/}
						</Container>
					</div>
				) : null}
			</main>
		</div>
	);
}
