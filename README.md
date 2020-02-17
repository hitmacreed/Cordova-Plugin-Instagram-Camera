# Cordova-Plugin-Instagram-Camera (Android ONLY)

## Example
<img src="https://github.com/hitmacreed/Cordova-Plugin-Instagram-Camera/blob/master/art/with_picker.png" width="400">
<img src="https://github.com/hitmacreed/Cordova-Plugin-Instagram-Camera/blob/master/art/without_picker.png" width="400">


## Installation

```sh
 cordova plugin add https://github.com/hitmacreed/Cordova-Plugin-Instagram-Camera.git
```

```sh
 ionic cordova plugin https://github.com/hitmacreed/Cordova-Plugin-Instagram-Camera.git
```

## Usage Ionic (3,4,5) 

 Working Example
```javascript

import { Component, OnInit } from '@angular/core';
import { File,FileEntry  } from '@ionic-native/file/ngx';
import { StreamingMedia } from '@ionic-native/streaming-media/ngx';
import { PhotoViewer } from '@ionic-native/photo-viewer/ngx';

declare var InstaCameraReader: any; // Import the plugin

var imgResult:any;
const MEDIA_FOLDER_NAME = Math.random().toString(36).substring(7);


@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page implements OnInit {
 

  imagePath:any;
  img:any;
  files:any[]=[];

  constructor( 
    private file: File,
    private streamingMedia: StreamingMedia,
    private photoViewer: PhotoViewer) {}

  ngOnInit(): void {
      this.loadFiles()
  }

  takePhoto() {

    var options = {
      showPicker: true,
      enableImageCropping: true,
      setVideoFileSize: 100,
    }

    InstaCameraReader.getPicture(

      options,

      function successCallback(objResult: any) {
        imgResult = objResult
      },

      function errorCallback(error: any) {
        imgResult = error
      },

    );

  }

  seePhoto() {
    const img = imgResult.result;
    this.processImg(img)
  }

  processImg(img:string){
    let path = this.file.dataDirectory;
    this.file.checkDir(path, MEDIA_FOLDER_NAME).then(() => {
      this.copyFileToLocalDir(img)
      },
      err => {
        this.file.createDir(path, MEDIA_FOLDER_NAME, false);

        this.file.writeFile(path,'img',img).then(res =>{
          console.log(res)
        }).catch(e =>{
          this.copyFileToLocalDir(img)
        }) 
      }
    );
  }

  copyFileToLocalDir(fullPath) {

    let myPath = fullPath;
    // Make sure we copy from the right location
    if (fullPath.indexOf('file://') < 0) {
      myPath = 'file://' + fullPath;
    }
 
    const ext = myPath.split('.').pop();
    const d = Date.now();
    const newName = `${d}.${ext}`;
 
    const name = myPath.substr(myPath.lastIndexOf('/') + 1);
    const copyFrom = myPath.substr(0, myPath.lastIndexOf('/') + 1);
    const copyTo = this.file.dataDirectory + MEDIA_FOLDER_NAME;
 
    this.file.copyFile(copyFrom, name, copyTo, newName).then(
      success => {
        this.loadFiles();
      },
      error => {
        console.log('error: ', error);
      }
    );
  }

  openFile(f: FileEntry) {
    if ( f.name.indexOf('.mp4') > -1) {
      this.streamingMedia.playVideo(f.nativeURL);
    } else if (f.name.indexOf('.jpg') > -1 || f.name.indexOf('.png') > -1) {
      this.photoViewer.show(f.nativeURL, 'MY awesome image');
    }
  }

  loadFiles() {
    this.file.listDir(this.file.dataDirectory, MEDIA_FOLDER_NAME).then(
      res => {
        this.files = res 
      },
      err => console.log('error loading files: ', err)
    );
  }
}
```
## Forked lib from Sandrios

#### [Sandrios](https://github.com/sandrios/sandriosCamera)
