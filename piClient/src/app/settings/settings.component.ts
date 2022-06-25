import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Settings } from '../model/settings';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css'],
})
export class SettingsComponent implements OnInit {
  settings: Settings;

  constructor(private cookieService: CookieService) {}

  ngOnInit(): void {
    const settingsString = this.cookieService.get('settings');
    this.settings = JSON.parse(settingsString);
  }

  save(): void {
    this.cookieService.set('settings', JSON.stringify(this.settings));
    window.location.reload();
  }
}
