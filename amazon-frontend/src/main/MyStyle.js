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
    },
    heroButtons: {
      marginTop: theme.spacing(4),
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
    footer: {
      backgroundColor: theme.palette.background.paper,
      padding: theme.spacing(6),
    },
  });