import { Dex, Pool } from "../../deps.ts";
import { connectionOptions, dialect } from "../../config/database.config.ts";
import { log } from "./logger_utils.ts";

const dbPool: Pool = new Pool({
  user: connectionOptions.user,
  password: connectionOptions.password,
  database: connectionOptions.database,
  hostname: connectionOptions.hostname,
  port: connectionOptions.port,
}, connectionOptions.poolSize);

/**
 * Query to connect to Choppa's database client to verify connectivity
 */
export async function runQuery(query: string) {
  const client = await dbPool.connect();
  const dbResult = await client.query(query);
  client.release();
  return dbResult;
}

/**
 * Attempt to connect to Choppa's database client to verify connectivity
 */
export async function dbConnection() {
  log.warn("Database", "Connecting to the database");
  const client = await dbPool.connect().catch((error) => {
    log.error("Database", error);
    throw error;
  });
  log.success("Database", "Successfully connected to the database!");
  client.release();
}

/**
 * Creates table in Choppa's database
 *
 * For table creation examples, please see: [Dex - Basic usage][1]
 * [1]:https://github.com/denjucks/dex#basic-usage
 *
 * @param name table name to create
 * @param table
 */
export function createTable(name: string, table: (table: unknown) => unknown) {
  return `${Dex.default({ client: dialect }).schema.createTable(name, table)}`;
}

/**
 * Drops table in Choppa's database
 *
 * @param name table name to drop
 */
export function dropTable(name: string) {
  return `${Dex.default({ client: dialect }).schema.dropTable(name)}`;
}
