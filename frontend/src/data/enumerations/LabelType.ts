export class LabelType {
  public static readonly LIGHT_RED_LABEL = new LabelType(
    "light-red-label",
    "light-white-text"
  );

  public static readonly LIGHT_GREEN_LABEL = new LabelType(
    "light-green-label",
    "light-white-text"
  );

  readonly backgroundColourCss: string;
  readonly textColourCss: string;

  private constructor(backgroundColour: string, textColour: string) {
    this.backgroundColourCss = backgroundColour;
    this.textColourCss = textColour;
  }
}