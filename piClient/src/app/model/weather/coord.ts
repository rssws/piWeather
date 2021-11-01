export class Coord {
  lon: number;
  lat: number;

  public getlon(): number {
    return this.lon;
  }

  public setlon(value: number): void {
    this.lon = value;
  }

  public getlat(): number {
    return this.lat;
  }

  public setlat(value: number): void {
    this.lat = value;
  }
}
