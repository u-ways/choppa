import {Pool, bold, gray, green, red, yellow} from "../../deps.ts";
import {connectionOptions} from "../../config/database.config.ts";

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
            console.error(bold(red(`Failed to connect to Choppa's Database: ${error.message}`)));
            throw error;
        });
    console.info(green(`${gray("[Database]")} Successfully connected to the database!`))
    client.release();
}