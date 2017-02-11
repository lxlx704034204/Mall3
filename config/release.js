var fs = require('fs');
var exec = require('child_process').exec;

var args = process.argv.splice(2);
console.log(args)
var cmd = 'java -jar ${WORKSPACE}/config/PackerNg-1.0.7.jar ${WORKSPACE}/bakApk/V'+`${args[0]}-${args[1]}-${args[2]}`
+'/hxqc/signed_7zip_aligned.apk ${WORKSPACE}/config/market.txt ${WORKSPACE}/bakApk/V'+`${args[0]}-${args[1]}-${args[2]}/hxqc`;
exec(cmd, function(err,stdout,stderr){
    if(err) {
        console.log('PackerNG'+stderr);
    } else {
        console.log("PackerNG "+stdout);
    }
});