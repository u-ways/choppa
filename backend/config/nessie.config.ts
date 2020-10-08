import { ClientPostgreSQL } from "https://raw.githubusercontent.com/halvardssm/deno-nessie/master/mod.ts";
import Dex from "https://raw.githubusercontent.com/denjucks/dex/master/mod.ts";
import { connectionOptions } from "./database.config.ts";

const nessieOptions = {
    migrationFolder: `${Deno.cwd()}/config/db/migrations`,
    seedFolder: `${Deno.cwd()}/config/db/seeds`,
};

export default {
    client: new ClientPostgreSQL(nessieOptions, connectionOptions),
    exposeQueryBuilder: false,
};

export const dex = Dex({client: "postgres"});

export { Migration } from "https://raw.githubusercontent.com/halvardssm/deno-nessie/master/mod.ts";

