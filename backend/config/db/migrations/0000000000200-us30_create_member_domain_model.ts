import { Migration } from "../../nessie.config.ts";
import { createTable, dropTable } from "../../../src/utils/db_utils.ts";

export const up: Migration = () => {
  return createTable("member", (table: any) => {
    table.increments("mem_id").primary();
    table.string("mem_name", 50).notNullable();
    table.integer("mem_chap").notNullable();
    table.foreign("mem_chap").references("chap_id").inTable("chapter");
    table.unique("mem_name");
  });
};

export const down: Migration = () => {
  return dropTable("member");
};
