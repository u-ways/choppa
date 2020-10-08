import {dex, Migration} from "../../nessie.config.ts";

export const up: Migration = () => {
    return dex.schema.createTable("chapter", (table: any) => {
        table.increments("chap_id").primary();
        table.string("chap_role", 50).notNullable();
        table.unique("chap_role");
    }).toString();
};