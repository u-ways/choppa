import { applyGraphQL, Router } from "../../deps.ts";

import { typeDefinitions } from "./definitions.graphql.ts";
import { resolvers } from "./resolvers.graphql.ts";
import {
  graphqlDebugger,
  graphqlEndPoint,
} from "../../config/graphql.config.ts";

const GraphQLService = await applyGraphQL<Router>({
  Router,
  path: graphqlEndPoint,
  typeDefs: typeDefinitions,
  resolvers: resolvers,
  usePlayground: graphqlDebugger,
});

export const gqlRoutes = GraphQLService.routes();
export const gqlMethods = GraphQLService.allowedMethods();
