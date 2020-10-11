/** GraphQL Query Resolvers ******************************/
import Member from "../core/domain/Member.ts";
import Squad from "../core/domain/Squad.ts";
import Tribe from "../core/domain/Tribe.ts";

const Query = {
  // Members Query Resolvers ///////////////////////////////////////////
  FindMemberById: (parent: Squad, { id }: Member) => ({
    "id": id,
    "name": `member-${id}`,
  }),

  // Squad Query Resolvers /////////////////////////////////////////////
  FindSquadById: (parent: Tribe, { id }: Squad) => ({
    "id": id,
    "name": `squad-${id}`,
    "members": [
      { "id": "mem_id", "name": "member-1" },
      { "id": "mem_id", "name": "member-2" },
    ],
  }),
};

/** GraphQL Mutation Resolvers ***************************/
const Mutation = {
  // Members Mutation Resolvers ////////////////////////////////////////
  CreateMember: (parent: Squad, { name }: Member) => (
    `id-of-${name}`
  ),

  // Squad Mutation Resolvers //////////////////////////////////////////
  CreateSquad: (parent: Tribe, { name }: Squad) => (
    `id-of-${name}`
  ),
};

/** GraphQL Query & Mutation Resolvers *******************/
export const resolvers = {
  Query: Query,
  Mutation: Mutation,
};
