import { createLocalVue, describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import HomePage from "@/_old/components/pages/HomePage";
import VueRouter from "vue-router";

describe("Home Page test", () => {
  it("Renders correctly", async () => {
    const localVue = createLocalVue();
    localVue.use(VueRouter);
    const router = new VueRouter();

    const wrapper = shallowMount(HomePage, {
      localVue,
      router,
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
