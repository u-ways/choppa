import { dex, Migration } from "../../nessie.config.ts";

export const up: Migration = () => {
    return dex.schema.createTable("tribe_current_members", (table: any) => {
        table.integer("tribe_id");
        table.integer("squad_id");
        table.primary(["tribe_id", "squad_id"]);
        table.foreign("tribe_id").references("tribe_id").inTable("tribe");
        table.foreign("squad_id").references("squad_id").inTable("squad");
    }).toString();
};