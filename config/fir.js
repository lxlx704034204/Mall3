var fs = require('fs');
var exec = require('child_process').exec; 

var args = process.argv.splice(2);
console.log(args)
var cmd = 'java -jar ${WORKSPACE}/config/PackerNg-1.0.7.jar ${WORKSPACE}/bakApk/V'+`${args[0]}-${args[1]}-${args[2]}`
+'/pgyer/signed_7zip_aligned.apk ${WORKSPACE}/config/market_fir.txt ${WORKSPACE}/bakApk/V'+`${args[0]}-${args[1]}-${args[2]}/pgyer`;
exec(cmd, function(err,stdout,stderr){
    if(err) {
        console.log('PackerNG'+stderr);
    } else {
        console.log("PackerNG "+stdout);

    }
});
// var args = process.argv.splice(2);
// var cmd = 'curl -F "file=@' + args[0] + '" -F "uKey=b7b7c394273b4a322c6c581454322ece" -F "_api_key=01a6c110c8ca950f35eb85722ae5d037" https://www.pgyer.com/apiv1/app/upload';
// exec(cmd, function(err,stdout,stderr){
//     if(err) {
//         console.log('Upload Error: '+stderr);
//     } else {        
//         var result = JSON.parse(stdout);
//         console.log("RCodeURL="+result.data.appQRCodeURL+"appVersion="+result.data.appVersion);
   
//     }
// });