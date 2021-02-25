import React from "react";
import ClayIcon from "@clayui/icon";
import { PluginInfo } from "@openk9/http-api";

export const pluginInfo: PluginInfo = {
  pluginId: "example-datasource",
  displayName: "Example DataSource",
  pluginType: ["DATASOURCE"],
  dataSourceAdminInterfacePath: {
    iconRenderer,
    settingsRenderer,
  },
  dataSourceRenderingInterface: {
    resultRenderers: {
      example: exampleResultRenderer,
    },
    sidebarRenderers: {
      example: exampleSidebarRenderer,
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

function exampleResultRenderer(props: any) {
  console.log("exampleResultRenderer", props);
  return (
    <>
      <h1>It Works!</h1>
    </>
  );
}

function exampleSidebarRenderer(props: any) {
  console.log("exampleSidebarRenderer", props);
  return null;
}
