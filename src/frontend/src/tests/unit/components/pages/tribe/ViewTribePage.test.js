import { createLocalVue, describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import VueRouter from "vue-router";
import ViewTribePage from "@/components/pages/tribe/ViewTribePage";

describe("View Tribe Page test", () => {
  it("Renders correctly", async () => {
    const localVue = createLocalVue();
    localVue.use(VueRouter);
    const router = new VueRouter();

    const wrapper = shallowMount(ViewTribePage, {
      localVue,
      router,
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
