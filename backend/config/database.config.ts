/**
 * Database connection options information
 */
interface ConnectionOptions {
    user: string;
    password: string;
    database: string;
    hostname: string;
    port: number;
    poolSize: number;
}

/**
 * Choppa's database connection options
 */
export const connectionOptions: ConnectionOptions = {
    user: Deno.env.get("DB_USERNAME") || "choppa",
    password: Deno.env.get("DB_PASSWORD") || "12345",
    database: Deno.env.get("DB_NAME") || "choppa",
    hostname: Deno.env.get("DB_HOST_URL") || "localhost",
    port: parseInt(Deno.env.get("DB_PORT") || "5432"),
    poolSize: parseInt(Deno.env.get("DB_POOL_CONNECTIONS") || "15")
};

