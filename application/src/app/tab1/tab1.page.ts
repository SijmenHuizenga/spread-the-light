import { Component } from '@angular/core';
import { Platform } from '@ionic/angular';
import { Flashlight } from '@ionic-native/flashlight';


@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page {

  constructor(private platform: Platform) {

  }

  toggleFlashlight() {
    this.platform.ready().then(() => {
      Flashlight.available().then((isAvailable :boolean) => {
        if (!isAvailable) {
          alert("Flashlight not available on this device");
          return;
        }
        Flashlight.switchOn()
    
        // switch off after 3 seconds
        setTimeout(function() {
          Flashlight.switchOff(); // success/error callbacks may be passed
        }, 3000);
      
      })
     });
  }

}
