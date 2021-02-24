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

function iconRenderer() {
  return <ClayIcon symbol="document" />;
}

function settingsRenderer() {
  return null;
}

function exampleResultRenderer() {
  return (
    <>
      <h1>It Works!</h1>
    </>
  );
}

function exampleSidebarRenderer() {
  return null;
}
