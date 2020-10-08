import {dex, Migration} from "../../nessie.config.ts";

export const up: Migration = () => {
    return dex.schema.createTable("iteration", (table: any) => {
        table.increments("iter_id").primary();
        table.integer("iter_no").notNullable();
        table.integer("iter_timebox").notNullable();
        table.timestamp("iter_date").notNullable();
    }).toString();
};