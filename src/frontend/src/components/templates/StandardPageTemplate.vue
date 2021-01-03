<template>
  <div class="min-h-screen flex flex-col">
    <div class="flex-none">
      <Navbar/>
    </div>
    <div class="flex-grow flex items-stretch">
      <slot name="whole-page">
        <div class="flex-grow w-full">
          <div class="bg-choppa-two px-5 py-10 text-white font-semibold">
            <slot name="page-header"/>
          </div>
          <slot name="full-width"/>
          <div class="container mx-auto max-w-screen-lg">
            <slot name="fixed-width"/>
          </div>
        </div>
      </slot>
    </div>
    <div class="flex-none">
      <Footer/>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/navbar/Navbar";
import Footer from "@/components/footer/Footer";

export default {
  name: "StandardPageTemplate",
  components: {
    Footer,
    Navbar,
  },
};
</script>
