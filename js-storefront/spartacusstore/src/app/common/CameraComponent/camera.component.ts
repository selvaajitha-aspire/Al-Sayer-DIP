import { Component, OnInit, ChangeDetectionStrategy, ViewChild, ElementRef, Renderer2, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { setFormField, isEmpty } from '../utility';

declare var $: any;
@Component({
    selector: 'app-camera-component',
    templateUrl: './camera.component.html',
    styleUrls: ['./camera.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
  })
  export class CameraComponent {

    constructor(private renderer : Renderer2){
        
    }

    captureFlag : boolean = true;
    retakeFlag : boolean = false;
    videoStream : MediaStream;

    @Input() formGrp: FormGroup;
    @Input() attachmentId: String;
    @Input() cameraFacing: string;

    @Output()
    getImage = new EventEmitter<any>();
    
    @ViewChild('video', { static: true }) videoElement: ElementRef;
    @ViewChild('canvas', { static: true }) canvas: ElementRef;

    constraints = {
      video: {
          facingMode: this.cameraFacing,
          width: { ideal:  window.innerWidth },
          height: { ideal: window.innerHeight }
      }
    };
    videoWidth = 0;
    videoHeight = 0;
    currentImage = '';
  
    openCamera(event){
        event.preventDefault();
        
        this.videoElement.nativeElement.style.display='block';
        this.canvas.nativeElement.style.display='none';
         this.retakeFlag = false;
        this.captureFlag = true;
        this.startCamera(event);
    }

    startCamera(event) {
        if (!!(navigator.mediaDevices && navigator.mediaDevices.getUserMedia)) { 
          navigator.mediaDevices.getUserMedia(this.constraints).then(this.attachVideo.bind(this)).catch(this.handleError);
        } else {
            alert('Sorry, camera not available.');
        }
      }
    handleError(error) {
      alert(error);
      }
    
    attachVideo(stream) {
        this.videoStream = stream;
        this.renderer.setProperty(this.videoElement.nativeElement, 'srcObject', stream);
        this.renderer.listen(this.videoElement.nativeElement, 'play', (event) => {
            this.videoHeight = this.videoElement.nativeElement.videoHeight;
            this.videoWidth = this.videoElement.nativeElement.videoWidth;
        });
        $("#cameraModal").modal('show');
      }
    
    capture(event) {
        event.preventDefault();
        this.videoElement.nativeElement.style.display='none';
        this.canvas.nativeElement.style.display='block';

        this.renderer.setProperty(this.canvas.nativeElement, 'width', this.videoWidth);
        this.renderer.setProperty(this.canvas.nativeElement, 'height', this.videoHeight);
        this.canvas.nativeElement.getContext('2d').drawImage(this.videoElement.nativeElement, 0, 0);

        this.retakeFlag = true;
        this.captureFlag = false;
      }
    
      retake(event){
        event.preventDefault();
        this.videoElement.nativeElement.style.display='block';
        this.canvas.nativeElement.style.display='none';

        this.retakeFlag = false;
        this.captureFlag = true;
      }

      useImage(event){
        event.preventDefault();
        var formObj = this.formGrp; 
        var att = this.attachmentId;
        this.getImage.emit(this.canvas.nativeElement.toDataURL());
        this.canvas.nativeElement.toBlob(function(blob){
          console.log(blob);
          
          setFormField(formObj,att,blob);
        },'image/png');

        this.retakeFlag = false;
        this.captureFlag = true;
        this.videoStream.getTracks()[0].stop();

        $("#cameraModal").modal('hide');
      }

      downloadImage(event){
        event.preventDefault();
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
        this.videoStream.getTracks()[0].stop();

        $("#cameraModal").modal('hide');
      }

      stopCamera(event){
        event.preventDefault();
        this.retakeFlag = false;
        this.captureFlag = true;
        if(!isEmpty(this.videoStream)) {
          this.videoStream.getTracks()[0].stop();
        }
        

        $("#cameraModal").modal('hide');
      }
      
  }