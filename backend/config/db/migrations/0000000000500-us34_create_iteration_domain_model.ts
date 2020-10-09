import {Migration} from "../../nessie.config.ts";
import {createTable, dropTable} from "../../../src/utils/db_utils.ts";

export const up: Migration = () => {
    return createTable("iteration", (table: any) => {
        table.increments("iter_id").primary();
        table.integer("iter_no").notNullable();
        table.integer("iter_timebox").notNullable();
        table.timestamp("iter_date").notNullable();
    });
};

export const down: Migration = () => {
    return dropTable("iteration");
};