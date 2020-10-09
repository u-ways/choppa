import {bold, Dex, gray, green, Pool, red, yellow} from "../../deps.ts";
import {connectionOptions, dialect} from "../../config/database.config.ts";

const dbPool: Pool = new Pool({
    user: connectionOptions.user,
    password: connectionOptions.password,
    database: connectionOptions.database,
    hostname: connectionOptions.hostname,
    port: connectionOptions.port
}, connectionOptions.poolSize);

/**
 * Query to connect to Choppa's database client to verify connectivity
 */
export async function runQuery(query: string) {
    const client = await dbPool.connect();
    const dbResult = await client.query(query);
    client.release();
    return dbResult
}

/**
 * Attempt to connect to Choppa's database client to verify connectivity
 */
export async function dbConnection() {
    console.info(yellow(`${gray("[Database]")} Connecting to the database`))
    const client = await dbPool
        .connect()
        .catch(error => {
            console.error(bold(gray("[Database]") + red(`Failed to connect to Choppa's Database: ${error.message}`)));
            throw error;
        });
    console.info(green(`${gray("[Database]")} Successfully connected to the database!`))
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
export function createTable(name: string, table: (table: any) => any) {
    return Dex.default({client: dialect}).schema.createTable(name, table).toString();
}

/**
 * Drops table in Choppa's database
 *
 * @param name table name to drop
 */
export function dropTable(name: string) {
    return Dex.default({client: dialect}).schema.dropTable(name).toString();
}