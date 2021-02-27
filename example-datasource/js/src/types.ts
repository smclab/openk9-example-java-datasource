import { GenericResultItem } from "@openk9/http-api";

export type ExampleResultItem = GenericResultItem<{
  example: {
    title: string;
    description: string;
  };
}>;
