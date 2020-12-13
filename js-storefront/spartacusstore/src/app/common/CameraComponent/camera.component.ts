import { Component, OnInit, ChangeDetectionStrategy, ViewChild, ElementRef, Renderer2 } from '@angular/core';

declare var $: any;
@Component({
    selector: 'app-camera-component',
    templateUrl: './camera.component.html',
    //styleUrls: ['./camera.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
  })
  export class CameraComponent {

    constructor(private renderer : Renderer2){
        
    }

    captureFlag : boolean = true;
    retakeFlag : boolean = false;
    videoStream : MediaStream;

    @ViewChild('video', { static: true }) videoElement: ElementRef;
    @ViewChild('canvas', { static: true }) canvas: ElementRef;
    constraints = {
      video: {
          facingMode: "environment",
          width: { ideal:  window.innerWidth },
          height: { ideal: window.innerHeight }
      }
    };
    videoWidth = 0;
    videoHeight = 0;
    currentImage = '';
  
    openCamera(){
        $("#cameraModal").modal('show');
        this.startCamera();
    }

    startCamera() {
        if (!!(navigator.mediaDevices && navigator.mediaDevices.getUserMedia)) { 
          navigator.mediaDevices.getUserMedia(this.constraints).then(this.attachVideo.bind(this)).catch(this.handleError);
        } else {
            alert('Sorry, camera not available.');
        }
      }
    handleError(error) {
        console.log('Error: ', error);
      }
    
    attachVideo(stream) {
        this.videoStream = stream;
        this.renderer.setProperty(this.videoElement.nativeElement, 'srcObject', stream);
        this.renderer.listen(this.videoElement.nativeElement, 'play', (event) => {
            this.videoHeight = this.videoElement.nativeElement.videoHeight;
            this.videoWidth = this.videoElement.nativeElement.videoWidth;
        });
      }
    
    capture() {
        this.videoElement.nativeElement.style.display='none';
        this.canvas.nativeElement.style.display='block';

        this.renderer.setProperty(this.canvas.nativeElement, 'width', this.videoWidth);
        this.renderer.setProperty(this.canvas.nativeElement, 'height', this.videoHeight);
        this.canvas.nativeElement.getContext('2d').drawImage(this.videoElement.nativeElement, 0, 0);

        this.retakeFlag = true;
        this.captureFlag = false;
      }
    
      retake(){
        this.videoElement.nativeElement.style.display='block';
        this.canvas.nativeElement.style.display='none';

        this.retakeFlag = false;
        this.captureFlag = true;
      }

      useImage(){
        this.canvas.nativeElement.toBlob(function(blob){
            let url = window.URL.createObjectURL(blob);
            let a = document.createElement('a');
            document.body.appendChild(a);
            a.setAttribute('style', 'display: none');
            a.href = url;
            a.download = "test";
            a.click();
            window.URL.revokeObjectURL(url);
            a.remove();
        },'image/png');

        this.retakeFlag = false;
        this.captureFlag = true;

        console.log(this.videoStream);
        this.videoStream.getTracks()[0].stop();

        $("#cameraModal").modal('hide');
      }

      stopCamera(){
        
        this.retakeFlag = false;
        this.captureFlag = true;

        this.videoStream.getTracks()[0].stop();

        $("#cameraModal").modal('hide');
      }
      
  }