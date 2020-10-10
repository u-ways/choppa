import { mount } from "@vue/test-utils";
import ChoppaLogoAtom from "../../../../components/atoms/ChoppaLogoAtom.vue";

// TODO BT: Is testing-library/vue ported to vue-next, yet? If so refactor this to use that.
describe("ChoppaLogoAtom test", () => {

    const fontAwesomeFakeComponent = {
        template: "<div>Test Component</div>"
    };

    test("Check css prop is applied as a class to the icon", () => {
        const wrapper = mount(ChoppaLogoAtom, {
            global: {
                components: {
                    "font-awesome": fontAwesomeFakeComponent
                }
            },
            props: {
                css: "test-css-class-one test-css-class-two"
            }
        });

        expect(wrapper.html()).toBe(
            "<div icon=\"helicopter\" class=\"test-css-class-one test-css-class-two\">" +
            "Test Component" +
            "</div>"
        );
    });

});