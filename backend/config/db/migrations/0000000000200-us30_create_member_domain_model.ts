import {dex, Migration} from "../../nessie.config.ts";

export const up: Migration = () => {
    return dex.schema.createTable("member", (table: any) => {
        table.increments("mem_id").primary();
        table.string("mem_name", 50).notNullable();
        table.integer("mem_chap").notNullable();
        table.foreign("mem_chap").references("chap_id").inTable("chapter");
        table.unique("mem_name");
    }).toString();
};