import {ClientPostgreSQL} from "../deps.ts";
import {connectionOptions} from "./database.config.ts";

const nessieOptions = {
    migrationFolder: `${Deno.cwd()}/config/db/migrations`,
    seedFolder: `${Deno.cwd()}/config/db/seeds`,
};

export default {
    client: new ClientPostgreSQL(nessieOptions, connectionOptions),
    exposeQueryBuilder: false,
};

export {Migration} from "../deps.ts";