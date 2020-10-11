import { Migration } from "../../nessie.config.ts";
import { createTable, dropTable } from "../../../src/utils/db_utils.ts";

export const up: Migration = () => {
  return createTable("chapter", (table: any) => {
    table.increments("chap_id").primary();
    table.string("chap_role", 50).notNullable();
    table.unique("chap_role");
  });
};

export const down: Migration = () => {
  return dropTable("chapter");
};
