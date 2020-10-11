export class Environment {
  private static readonly DEV_ENV: string = "development";

  public static isDevelopment(): boolean {
    return process.env.NODE_ENV === this.DEV_ENV;
  }
}