var fs = require('fs');
var process = require('process');
var basePath = __dirname + '/../';
var apksPath =  basePath + 'apks/';
var mappingFilename = 'mapping.txt';
var mappingPath = basePath + 'app/build/outputs/mapping/hxqc/release/'+mappingFilename;
var resFilename = 'resource_mapping_app-hxqc-release.txt';
var resPath = basePath + 'app/build/outputs/apk/AndResGuard_app-hxqc-release/' + resFilename;

var arguments = process.argv.splice(2);
var version = arguments[0];

fs.readdir(apksPath, (err, files) =>{
	for (var i = 0 ; i < files.length ; i++) {
		var file = files[i];
		var parts = file.split('.');
		// 后缀不为APK，跳过。
		if (parts[parts.length-1] !== 'apk') 
			continue;
		
		parts = file.split('-');
		var newFile = `${parts[0]}-${parts[1]}-${parts[parts.length - 1]}`;

		fs.renameSync(apksPath+file, apksPath+newFile);		
 	}


	fs.renameSync(mappingPath, apksPath + mappingFilename);
	fs.renameSync(resPath, apksPath + resFilename);

	var date = new Date().format('yyMMdd-hhmmss');
	fs.renameSync(apksPath, basePath+'apks_'+version+'_'+date);	
	console.log('完成');
 });



Date.prototype.format = function (fmt) { //author: meizz 
	var o = {
	    "M+": this.getMonth() + 1, //月份 
	    "d+": this.getDate(), //日 
	    "h+": this.getHours(), //小时 
	    "m+": this.getMinutes(), //分 
	    "s+": this.getSeconds(), //秒 
	    "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	    "S": this.getMilliseconds() //毫秒 
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
