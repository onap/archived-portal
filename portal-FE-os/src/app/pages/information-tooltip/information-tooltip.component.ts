import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-information-tooltip',
  templateUrl: './information-tooltip.component.html',
  styleUrls: ['./information-tooltip.component.scss']
})
export class InformationTooltipComponent implements OnInit {

  @Input() textMessage : any;
  constructor() { }

  message : string;
  ngOnInit() {
    console.log("Message ", this.textMessage);
    this.message = this.textMessage;
  }

}
