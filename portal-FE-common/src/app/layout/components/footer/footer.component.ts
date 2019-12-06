import { Component, OnInit } from '@angular/core';
import { ManifestService } from 'src/app/shared/services';


@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  buildVersion: string;
  constructor(private manifest: ManifestService) { }

  ngOnInit() {
    this.buildVersion =  '';
    this.manifestDetails();
  }

  manifestDetails() {
    this.manifest.getManifest().subscribe((_res: any) => {
      this.buildVersion = _res.manifest['Build-Number'];
    }, (_err) => {

    });
  }
}
