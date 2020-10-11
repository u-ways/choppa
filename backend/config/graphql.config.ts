export const graphqlEndPoint = Deno.env.get("GRAPHQL_ENDPOINT") || "/gql";
export const graphqlDebugger = Deno.env.get("ENVIRONMENT") === "development";
