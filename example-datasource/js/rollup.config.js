import path from "path";
import babel from "@rollup/plugin-babel";
import resolve from "@rollup/plugin-node-resolve";
import json from "@rollup/plugin-json";
import replace from "@rollup/plugin-replace";
import commonjs from "@rollup/plugin-commonjs";
import externalGlobals from "rollup-plugin-external-globals";
import { terser } from "rollup-plugin-terser";

const root = process.platform === "win32" ? path.resolve("/") : "/";
const external = (id) => !id.startsWith(".") && !id.startsWith(root);
const extensions = [".js", ".jsx", ".ts", ".tsx", ".json"];

const getBabelOptions = ({ useESModules }, targets) => ({
  babelrc: false,
  extensions,
  exclude: "**/node_modules/**",
  babelHelpers: "runtime",
  presets: [
    ["@babel/preset-env", { loose: true, modules: false, targets }],
    "@babel/preset-react",
    "@babel/preset-typescript",
  ],
  plugins: [["@babel/transform-runtime", { regenerator: false, useESModules }]],
});

const externalGlobalsDefault = {
  react: "React",
  "@openk9/http-api": "ok9API",
  "@openk9/search-ui-components": "ok9Components",
};

export default [
  {
    input: `./src/index.tsx`,
    output: { file: `build/index.js`, format: "esm" },
    external: Object.keys(externalGlobalsDefault),
    plugins: [
      json(),
      babel(
        getBabelOptions(
          { useESModules: true },
          ">1%, not dead, not ie 11, not op_mini all",
        ),
      ),
      resolve({ extensions }),
      commonjs(),
      externalGlobals(externalGlobalsDefault),
      replace({
        "process.env.NODE_ENV": JSON.stringify("production"),
      }),
      terser(),
    ],
  },
  {
    input: `./src/index.tsx`,
    output: { file: `build/index.cjs.js`, format: "cjs" },
    external: Object.keys(externalGlobalsDefault),
    plugins: [
      json(),
      babel(getBabelOptions({ useESModules: false })),
      resolve({ extensions }),
      commonjs(),
      externalGlobals(externalGlobalsDefault),
      replace({
        "process.env.NODE_ENV": JSON.stringify("production"),
      }),
      terser(),
    ],
  },
];
