import { createLocalVue, describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import VueRouter from "vue-router";
import EditTribePage from "@/components/pages/tribe/EditTribePage";

describe("Edit Tribe Page test", () => {
  it("Renders correctly", async () => {
    const localVue = createLocalVue();
    localVue.use(VueRouter);
    const router = new VueRouter();

    const wrapper = shallowMount(EditTribePage, {
      localVue,
      router,
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
