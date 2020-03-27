import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { LoadingController } from '@ionic/angular';

@Component({
  selector: 'app-tab2',
  templateUrl: 'tab2.page.html',
  styleUrls: ['tab2.page.scss']
})
export class Tab2Page implements AfterViewInit {
  @ViewChild('video', { static: false }) video: ElementRef;
  @ViewChild('canvas', { static: false }) canvas: ElementRef;

  // Create class attributes
  canvasElement: any;
  videoElement: any;
  canvasContext: any;
  scanActive = false;
  loading: HTMLIonLoadingElement = null;

  constructor(
    private loadingCtrl: LoadingController
  ) {}

  /**
   * Sets attributes after view has been loaded
   */
  ngAfterViewInit() {
    this.canvasElement = this.canvas.nativeElement;
    this.canvasContext = this.canvasElement.getContext('2d');
    this.videoElement = this.video.nativeElement;
  }

  /**
   * Stop camera feed
   */
  stopScan() {
    this.scanActive = false;
  }

  /**
   * Start the camera feed
   */
  async startScan() {
    // Start camera stream on front camera
    const stream = await navigator.mediaDevices.getUserMedia({
      video: { facingMode: 'environment' }
    });

    // Set camera stream as source video DOM element
    this.videoElement.srcObject = stream;

    // Show loading animation
    this.loading = await this.loadingCtrl.create({});
    await this.loading.present();

    this.videoElement.play();
    requestAnimationFrame(this.scan.bind(this));
  }

  /**
   * Keep updating the view until stopScan method is called
   */
  async scan() {
    if (this.videoElement.readyState === this.videoElement.HAVE_ENOUGH_DATA) {
      // Close loading animation
      if (this.loading) {
        await this.loading.dismiss();
        this.loading = null;
        this.scanActive = true;
      }

      // Set canvas parameters
      this.canvasElement.height = this.videoElement.videoHeight;
      this.canvasElement.width = this.videoElement.videoWidth;

      // Draw video images on canvas
      this.canvasContext.drawImage(this.videoElement, 0, 0, this.canvasElement.width, this.canvasElement.height);
      const imageData = this.canvasContext.getImageData(0, 0, this.canvasElement.width, this.canvasElement.height);

      // Repeat
      if (this.scanActive) {
        requestAnimationFrame(this.scan.bind(this));
      }
    }
  }
}
