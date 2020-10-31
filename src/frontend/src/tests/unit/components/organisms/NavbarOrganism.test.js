import { createLocalVue, describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import VueRouter from "vue-router";
import NavbarOrganism from "@/components/organisms/NavbarOrganism";

describe("Navbar Organism test", () => {
  it("Renders correctly", async () => {
    const localVue = createLocalVue();
    localVue.use(VueRouter);
    const router = new VueRouter();

    const wrapper = shallowMount(NavbarOrganism, {
      localVue,
      router,
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
