var exec = require('child_process').exec; 
//var cmd = 'git reset --hard';
//exec(cmd, function(err,stdout,stderr){
//    if(err) {
//        console.log('Upload Error: '+stderr);
//    } else {
//        console.log(stdout);

       var basePath = __dirname + '/../';
       var projPath = basePath+'app/src/main/java/com/hxqc/mall/activity/MainActivity.java';
       var fs = require('fs');
       var proj = fs.readFileSync(projPath, 'utf-8');
       proj = proj.replace('PgyerUpdate.update(this);', '');
       proj = proj.replace('import com.hxqc.mall.core.util.PgyerUpdate;', '');
       fs.writeFileSync(projPath, proj); //

       fs.unlink(basePath+'app/src/main/java/com/hxqc/mall/core/util/PgyerUpdate.java')   //删除文件
       fs.unlink(basePath+'app/libs/pgyer_sdk_2.4.5.jar')   //删除文件
       fs.unlink(basePath+'app/src/main/java/com/hxqc/mall/DevDemoActivity.java')   //删除文件
       fs.unlink(basePath+'app/src/main/java/com/hxqc/mall/TestDemoActivity.java')   //删除文件
       fs.unlink(basePath+'app/src/main/java/com/hxqc/mall/TestMapNvaiActivity.java')   //删除文件
//    }
//});


