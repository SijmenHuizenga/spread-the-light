import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { LoadingController } from '@ionic/angular';

declare var cv: any;

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

  src: any;
  dstC1: any;
  dstC3: any;
  dstC4: any;
  vc: any;

  constructor(
    private loadingCtrl: LoadingController
  ) {}

  /**
   * Sets attributes after view has been loaded
   */
  ngAfterViewInit(): void {
    this.canvasElement = this.canvas.nativeElement;
    this.canvasContext = this.canvasElement.getContext('2d');
    this.videoElement = this.video.nativeElement;
  }

  /**
   * Stop camera feed
   */
  stopCamera(): void {
    this.scanActive = false;
  }

  /**
   * Start the camera feed
   */
  async startCamera(): Promise<void> {
    // Start camera stream on rear camera
    const stream = await navigator.mediaDevices.getUserMedia({
      video: { facingMode: 'environment' }
    });

    // Set camera stream as source of video DOM element
    this.videoElement.srcObject = stream;

    // Show loading animation
    this.loading = await this.loadingCtrl.create({});
    await this.loading.present();

    this.videoElement.play();
    this.vc = new cv.VideoCapture(this.videoElement);
    this.startVideoProcessing();
    // requestAnimationFrame(this.scan.bind(this));
  }

  async startVideoProcessing() {
    // Close loading animation
    if (this.loading) {
      await this.loading.dismiss();
      this.loading = null;
      this.scanActive = true;
    }

    const height = this.videoElement.videoHeight;
    const width = this.videoElement.videoWidth;

    this.src = new cv.Mat(height, width, cv.CV_8UC4);
    this.dstC1 = new cv.Mat(height, width, cv.CV_8UC1);
    this.dstC3 = new cv.Mat(height, width, cv.CV_8UC3);
    this.dstC4 = new cv.Mat(height, width, cv.CV_8UC4);
    requestAnimationFrame(this.processVideo.bind(this));
  }

  processVideo() {
    this.vc.read(this.src);
    let result;
    switch ('threshold') {
      case 'threshold': result = this.threshold(this.src); break;
      default: result = this.threshold(this.src);
    }
    cv.imshow(this.canvasElement, result);
    requestAnimationFrame(this.processVideo.bind(this));
  }

  threshold(src) {
    cv.threshold(src, this.dstC4, 10, 200, cv.THRESH_BINARY);
    return this.dstC4;
  }

  /**
   * Keep updating the view until stopScan method is called
   */
  async scan(): Promise<void> {
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
      console.log(this.canvasElement.height);
      console.log(this.canvasElement.width);

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
