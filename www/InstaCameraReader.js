var exec = require('cordova/exec');

var showPicker = null;
var enableImageCropping = null;
var setVideoFileSize = null;

function InstaCamera() {
}

var params = {
    showPicker: showPicker,
    enableImageCropping : enableImageCropping,
    setVideoFileSize:setVideoFileSize
}

/**
 * GetPicture from InstaCamera.
 * @param {function} successCallback This function will recieve a result object: { result:'media.getPath()'}
 * @param {function} errorCallback
 * @param options
 */


InstaCamera.prototype.getPicture = function (params,successCallback, errorCallback) {
   
   var options = {};
    
    options.showPicker = params.showPicker; 
    options.enableImageCropping = params.enableImageCropping;
    options.setVideoFileSize = params.setVideoFileSize;

  

    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("InstaCameraReader.getPicture failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("InstaCameraReader.getPicture failure: success callback parameter must be a function");
        return;
    }

      exec(
        function (objResult) {
            successCallback(objResult);
            
        },
        function(error) {
            errorCallback(error);
            
        },
        'InstaCameraReader',
        'getPicture',
        [options],
    );
}
   
module.exports = new InstaCamera();