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
    console.log("toggleFlashlight called");
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

        // Read input from the message field
        var input = this.inputValue

        // Tranform ASCII input to binary array
        function ASCII_2_Binary() {
          console.log("ASCII_2_Binary called");
          var output = "";
          // String of all the characters in binary separated by spaces
          for (var i = 0; i < input.length; i++) {
              output += input[i].charCodeAt(0).toString(2) + " ";
          }
          console.log(output);
          // Convert the string to a array of numbers
          // TODO:
              // Right now the space between characters is written as a zero
              // -> Write it with a different number, or leave the space
          var array = output.split('').map(Number);
          console.log(array);
          return array;
        }


        var array = ASCII_2_Binary();

        // Variables holding the lengths of the light pulses in ms
        var light_ON = 300;
        var light_OFF = 300;
        var different_character = 450;
        
        // Flash the message
        // TODO: add else if for space between characters
        function flashMessage() {
          (async () => { 
            for (let i of array){
              if (i == 1){
              Flashlight.switchOn();
              await delay(light_ON);
              Flashlight.switchOff();
              }
              else if (i ==0){
              await delay(light_OFF);
              }
            }
          })();
        }
        
        flashMessage();
      })
    });
  }

}
