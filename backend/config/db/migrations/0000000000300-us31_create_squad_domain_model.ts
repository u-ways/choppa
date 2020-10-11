import {dex, Migration} from "../../nessie.config.ts";

export const up: Migration = () => {
    return dex.schema.createTable("squad", (table: any) => {
        table.increments("squad_id").primary();
        table.string("squad_name", 50).notNullable();
        table.unique("squad_name");
    }).toString();
};