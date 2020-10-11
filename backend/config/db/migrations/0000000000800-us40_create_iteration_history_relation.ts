import { Migration } from "../../nessie.config.ts";
import { createTable, dropTable } from "../../../src/utils/db_utils.ts";

export const up: Migration = () => {
  return createTable("iteration_history", (table: any) => {
    table.integer("iter_id");
    table.integer("tribe_id");
    table.integer("squad_id");
    table.integer("mem_id");
    table.primary(["iter_id", "tribe_id", "squad_id", "mem_id"]);
    table.foreign("iter_id").references("iter_id").inTable("iteration");
    table.foreign("tribe_id").references("tribe_id").inTable("tribe");
    table.foreign("squad_id").references("squad_id").inTable("squad");
    table.foreign("mem_id").references("mem_id").inTable("member");
  });
};

export const down: Migration = () => {
  return dropTable("iteration_history");
};
