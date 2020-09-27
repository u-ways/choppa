import { MongoClient } from '../deps.ts';

class DB {
    public client: MongoClient;
    constructor(public dbName: string, public url: string) {
        this.dbName = dbName;
        this.url = url;
        this.client = {} as MongoClient;
    }
    connect() {
        const client = new MongoClient();
        client.connectWithUri(this.url);
        this.client = client;
    }
    get getDatabase() {
        return this.client.database(this.dbName);
    }
}

const dbName = Deno.env.get("DB_NAME") || "local_db";
const dbHostUrl = Deno.env.get("DB_HOST_URL") || "mongodb://localhost:27017";

const db = new DB(dbName, dbHostUrl);
db.connect();

export default db;

// TODO(u-ways): switch to DenoDB if too much boilerplate code is needed:
//  https://deno.land/x/denodb@v1.0.9

// TODO(u-ways): include db sync (drop on dev env)
//   e.g. await db.sync({ drop: true });

// TODO(u-ways): note down db.close() best practices