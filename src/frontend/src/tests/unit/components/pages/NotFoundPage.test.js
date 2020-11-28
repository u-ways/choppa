import { createLocalVue, describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import VueRouter from "vue-router";
import NotFoundPage from "@/_old/components/pages/NotFoundPage";

describe("Not Found Page test", () => {
  it("Renders correctly", async () => {
    const localVue = createLocalVue();
    localVue.use(VueRouter);
    const router = new VueRouter();

    const wrapper = shallowMount(NotFoundPage, {
      localVue,
      router,
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
