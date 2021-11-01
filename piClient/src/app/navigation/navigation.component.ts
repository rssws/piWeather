import { Component, OnInit } from '@angular/core';
import { version } from '../../../package.json';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  version: string = version;
  today: number = Date.now();

  constructor() {}

  ngOnInit(): void {
    setInterval(() => {
      this.today = Date.now();
    }, 1000);
  }
}
