<template>
  <div class="flex flex-col text-center place-items-center pr-3 pt-12 lg:pt-0">
    <div class="flex flex-col text-center place-items-center pr-3 lg:pt-0">
      <div class="grid w-16 h-16 rounded-full bg-choppa-two">
        <slot name="icon"/>
      </div>
      <div class="pt-3">
        <p class="text-xl sm:text-2xl lg:text-3xl font-bold order-first lg:order-last">
          <slot name="heading"/>
        </p>
        <p class="text-md sm:text-lg lg:text-xl">
          <slot name="body"/>
        </p>
      </div>
    </div>
  </div>
</template>
