import React from "react";
import { createUseStyles } from "react-jss";

import { ThemeType, Highlight, ResultCard } from "@openk9/search-ui-components";
import { ExampleResultItem } from "./types";
import ClayIcon from "@clayui/icon";

const useStyles = createUseStyles((theme: ThemeType) => ({
  root: {
    "&:focus, &:hover h4 :first-child": {
      textDecoration: "underline",
    },
  },
  iconArea: {
    margin: theme.spacingUnit,
    marginRight: theme.spacingUnit * 2,
    fontSize: 24,
  },
  title: {
    display: "flex",
    alignItems: "center",
    marginBottom: 0,
  },
  textArea: {
    fontSize: 14,
  },
}));

export function ExampleResultCard({
  data,
  className,
  ...rest
}: {
  data: ExampleResultItem;
  onSelect?: () => void;
} & React.AnchorHTMLAttributes<HTMLAnchorElement>): JSX.Element {
  const classes = useStyles();

  return (
    <ResultCard
      href={`https://www.google.com/search?q=${data.source.example.title}`}
      target="_blank"
      className={classes.root}
      {...rest}
    >
      <div className={classes.iconArea}>
        <ClayIcon symbol="document" />
      </div>
      <div style={{ minWidth: 0 }}>
        <h4 className={classes.title}>
          <Highlight
            text={data.source.example.title}
            highlight={data.highlight["example.title"]}
          />
        </h4>
        <div className={classes.textArea}>
          <Highlight
            text={data.source.example.description}
            highlight={data.highlight["example.description"]}
          />
        </div>
      </div>
    </ResultCard>
  );
}
