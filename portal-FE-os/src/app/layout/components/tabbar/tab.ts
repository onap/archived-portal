import { SafeUrl } from '@angular/platform-browser';

export class Tab {
  label: string;
  url: SafeUrl;
  active: boolean;

  constructor(label: string) {
      this.label = label;
  }

}
