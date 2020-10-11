import type Squad from "./Squad.ts";

export default interface Tribe {
  readonly id: string;
  readonly name: string;
  readonly squads: [Squad];
}
