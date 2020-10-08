import {dex, Migration} from "../../nessie.config.ts";

export const up: Migration = () => {
    return dex.schema.createTable("tribe", (table: any) => {
        table.increments("tribe_id").primary();
        table.string("tribe_name", 50).notNullable();
        table.unique("tribe_name");
    }).toString();
};