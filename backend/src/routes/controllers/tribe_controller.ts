import {Router, helpers} from "../../../deps.ts";

const tribeRouter = new Router();

tribeRouter.get('/tribe/:id', (ctx) => {
    const { id } = helpers.getQuery(ctx, { mergeParams: true });
    ctx.response.headers.set("Content-Type", "application/json");
    ctx.response.body = { "_id" : id, "name": `tribe-${id}` };
});

export default tribeRouter;