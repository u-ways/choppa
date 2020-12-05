<template>
  <StandardPageTemplate>
    <template v-slot:page-header>
      <div class="flex flex-row place-content-between place-items-center">
        <div>
          <p class="text-3xl font-normal">
            <span class="hidden sm:inline">Tribe </span>
            <span class="font-bold">Untitled</span>
          </p>
        </div>
        <div>
          <StyledButton type="link" variant="secondary-light" css="pr-5 pl-4"
                        link="/tribes/00000000-0000-0000-0000-000000000001/"
                        :link-append="true">
            <div class="inline pr-1">
              <font-awesome-icon icon="eye"/>
            </div>
            View
          </StyledButton>
        </div>
      </div>
    </template>
    <template v-slot:fixed-width>
      <div class="p-3">
        blah
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import StyledButton from "@/components/atoms/buttons/StyledButton";

export default {
  name: "ViewTribePage",
  components: {
    StandardPageTemplate,
    StyledButton,
  },
};
</script>
