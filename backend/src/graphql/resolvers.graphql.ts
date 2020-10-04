/** GraphQL Query Resolvers ******************************/
const Query = {
    // Members Query Resolvers ///////////////////////////////////////////
    FindMemberById: (parent: any, {id}: any, context: any, info: any) => ({
        "id": id,
        "name": `member-${id}`
    }),

    // Squad Query Resolvers /////////////////////////////////////////////
    FindSquadById: (parent: any, {id}: any, context: any, info: any) => ({
        "id": id,
        "name": `squad-${id}`,
        "members": [{"id": "mem_id", "name": "member-1"}, {"id": "mem_id", "name": "member-2"}]
    }),
}

/** GraphQL Mutation Resolvers ***************************/
const Mutation = {
    // Members Mutation Resolvers ////////////////////////////////////////
    CreateMember: (parent: any, {name}: any, context: any, info: any) => (
        `id-of-${name}`
    ),

    // Squad Mutation Resolvers //////////////////////////////////////////
    CreateSquad: (parent: any, {name}: any, context: any, info: any) => (
        `id-of-${name}`
    ),
}

/** GraphQL Query & Mutation Resolvers *******************/
export const resolvers = {
    Query: Query,
    Mutation: Mutation
}