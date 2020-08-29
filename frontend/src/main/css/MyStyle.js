import { fade } from "@material-ui/core/styles";

export const useStyles = (theme) => ({
	icon: {
		marginRight: theme.spacing(2),
	},
	title: {
		flexGrow: 1,
	},
	heroContent: {
		// backgroundColor: theme.palette.background.paper,
		padding: theme.spacing(3, 0, 2),
		borderColor:"black"
	},
	heroButtons: {
		marginTop: theme.spacing(4),
	},
	main: {
		// backgroundColor: "white",
		paddingTop:"60px",
		minHeight: "100vh",
	},
	cardGrid: {
		paddingTop: theme.spacing(4),
		paddingBottom: theme.spacing(8),
	},
	card: {
		height: "100%",
		display: "flex",
		flexDirection: "column",
	},
	cardMedia: {
		// paddingTop: "56.25%", // 16:9 56.25%
		height: 200,
	},
	cardContent: {
		flexGrow: 1,
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
});
