import { Migration } from "../../nessie.config.ts";
import { createTable, dropTable } from "../../../src/utils/db_utils.ts";

export const up: Migration = () => {
  return createTable("squad", (table: any) => {
    table.increments("squad_id").primary();
    table.string("squad_name", 50).notNullable();
    table.unique("squad_name");
  });
};

export const down: Migration = () => {
  return dropTable("squad");
};
