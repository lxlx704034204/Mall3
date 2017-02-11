
var exec = require('child_process').exec; 
var cmd = 'git reset --hard';
exec(cmd, function(err,stdout,stderr){
    if(err) {
        console.log('Upload Error: '+stderr);
    } else {        
       
        console.log(stdout);
   
    }
});
