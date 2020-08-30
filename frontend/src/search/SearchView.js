import React from "react";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import { fade, makeStyles } from "@material-ui/core/styles";
import { Link } from "react-router-dom";
import Pagination from "@material-ui/lab/Pagination";
import { withRouter } from "react-router-dom";
import { getCurrentUser, searchProducts } from "../util/APIUtils";

import Product from "../main/Product";

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
	heroContent: {
		// backgroundColor: theme.palette.background.paper,
		padding: theme.spacing(3, 0, 2),
		borderColor: "black",
	},
	pagination: {
		display: "flex",
		justifyContent: "center",
		alignItems: "center",
		margin: theme.spacing(4),
	},
}));

function SearchView(props) {
	const classes = useStyles();
	const [currentUser, setCurrentUser] = React.useState(null);
	const [searchText, setSearchText] = React.useState("");
	const [resultList, setResultList] = React.useState([]);
	const [page, setPage] = React.useState(1);
	const [totalPage, setTotalPage] = React.useState(10);

	const handlePageChange = (event, value) => {
		setPage(value);
		const parsed = queryString.parse(props.location.search);
		const q = parsed.q;
		props.history.push("/search?q=" + q + "&page=" + value);
	};

	React.useEffect(() => {
		let isCancelled = false;
		const searchResult = async (searchRequest) => {
			await searchProducts(searchRequest).then((res) => {
				if (!isCancelled) {
					setResultList(res.searchResult);
					setTotalPage(res.totalPages);
					console.log(res);
				}
			});
		};

		if (currentUser) {
			const parsed = queryString.parse(props.location.search);
			const q = parsed.q;
			const pageNum = parsed.page ? parsed.page : 1;
			const searchRequest = {
				userId: currentUser.userId,
				keyword: q,
				page: pageNum - 1,
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
							<div className={classes.heroContent}>
								<Typography component="h1" variant="h2" align="center" color="textPrimary" gutterBottom>
									<Box m={1}>Search Result</Box>
								</Typography>
							</div>
							<Grid container spacing={2}>
								{resultList
									? resultList.map((product, index) => (
											<Grid key={index} item xs={3}>
												<Box component="div" m={2} height="90%">
													<Product product={product} />
												</Box>
											</Grid>
									  ))
									: null}
							</Grid>
						</Container>
					</div>
				) : null}
				<div className={classes.pagination}>
					<Pagination count={totalPage} color="primary" page={page} onChange={handlePageChange} />
				</div>
			</main>
		</div>
	);
}

export default withRouter(SearchView);
