/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
 
import { Component, OnInit } from '@angular/core';
import { ManifestService } from 'src/app/shared/services';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  buildVersion: string;
  api = environment.api;
  brandName: string;
  footerLink: string;
  footerLinkText: string;
  footerMessage: string;
  footerLogoImagePath: string;
  footerLogoText: string;

  constructor(private manifest: ManifestService) { }

  ngOnInit() {
    this.buildVersion =  '';
    this.manifestDetails();

    this.brandName = "ONAP Portal";
    if(this.api.brandName != ''){
        this.brandName = this.api.brandName;
    }
    this.footerLink = this.api.footerLink;
    this.footerLinkText = this.api.footerLinkText;
    this.footerMessage= this.api.footerMessage;
    if(this.api.footerLogoImagePath !=''){
      this.footerLogoImagePath= this.api.footerLogoImagePath;
    }
    this.footerLogoText= this.api.footerLogoText;
  }

  manifestDetails() {
    this.manifest.getManifest().subscribe((_res: any) => {
      this.buildVersion = _res.manifest['Build-Number'];
    }, (_err) => {

    });
  }
}
