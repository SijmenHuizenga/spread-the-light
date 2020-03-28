import { Component } from '@angular/core';
import { Platform } from '@ionic/angular';
import { Flashlight } from '@ionic-native/flashlight';


@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page {

  inputValue: string = "";
  constructor(private platform: Platform) {

  }

  toggleFlashlight() {
    this.platform.ready().then(() => {
      Flashlight.available().then((isAvailable :boolean) => {
        if (!isAvailable) {
          alert("Flashlight not available on this device");
          return;
        }

        // Define the delay function
        function delay(ms: number) {
          return new Promise( resolve => setTimeout(resolve, ms) );
      }

      // Read the content of the message field and convert to int
      // TODO: function to encode alphanumerical characters into a binary sequence
      var number_flashes = +this.inputValue;
      function flashMessage() {
        (async () => { 
          for (let i = 0; i < number_flashes; i++) {
            Flashlight.switchOn();
            await delay(300);
            Flashlight.switchOff();
            await delay(300);
          }
        })();
      }
        
        flashMessage();
      })
     });
  }

}
