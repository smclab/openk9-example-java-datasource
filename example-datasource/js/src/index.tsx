import React from "react";
import ClayIcon from "@clayui/icon";
import { Plugin } from "@openk9/http-api";

import { ExampleResultCard } from "./ExampleResultCard";
import { ExampleResultItem } from "./types";

export const plugin: Plugin<ExampleResultItem> = {
  pluginId: "example-datasource",
  displayName: "Example DataSource",
  pluginType: ["DATASOURCE"],
  dataSourceAdminInterfacePath: {
    iconRenderer,
    settingsRenderer,
  },
  dataSourceRenderingInterface: {
    resultRenderers: {
      example: ExampleResultCard as any,
    },
    sidebarRenderers: {
      example: exampleSidebarRenderer as any,
    },
  },
};

function iconRenderer(props: any) {
  console.log("iconRenderer", props);
  return <ClayIcon symbol="document" />;
}

function settingsRenderer(props: any) {
  console.log("settingsRenderer", props);
  return (
    <>
      <h1>Settings Panel</h1>
    </>
  );
}

function exampleSidebarRenderer(props: any) {
  console.log("exampleSidebarRenderer", props);
  return null;
}
