import { Migration } from "../../nessie.config.ts";
import { createTable, dropTable } from "../../../src/utils/db_utils.ts";

export const up: Migration = () => {
  return createTable("tribe", (table: any) => {
    table.increments("tribe_id").primary();
    table.string("tribe_name", 50).notNullable();
    table.unique("tribe_name");
  });
};

export const down: Migration = () => {
  return dropTable("tribe");
};
