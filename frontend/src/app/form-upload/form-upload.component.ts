import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadFileService } from '../upload-file.service';
declare var Logger: any;
declare var log: any;

@Component({
  selector: 'form-upload',
  templateUrl: './form-upload.component.html',
  styleUrls: ['./form-upload.component.css']
})
export class FormUploadComponent implements OnInit {

  data: any = [];

  selectedFiles: FileList
  currentFileUpload: File
  progress: { percentage: number } = { percentage: 0 }

  constructor(private uploadService: UploadFileService) { }

  ngOnInit() {
    Logger.open();

    // setInterval(() => {
    //   const date = new Date();
    //   log(date.getTime());
    // }, 1000)

  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {
    log('uploading csv file');
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0)
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);        
      } else if (event instanceof HttpResponse) {
        log('uploading completed');
        setTimeout(() => {
          log('Preparing for batch job');
          log('Starting batch job csv to database');
          this.uploadService.triggerJob(event.body).subscribe(res => {
            log('Batch job completed with success');
            this.progress.percentage = 0;
            this.selectedFiles = undefined;    
          })
        }, 2000);
      }
    });

    

  }
}
