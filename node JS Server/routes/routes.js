
var chgpass = require('config/chgpass'); 
var register = require('config/register'); 
var login = require('config/login'); 
var loginFaculty = require('config/loginFaculty'); 
var registerForum = require('config/registerForum'); 
var registerFaculty = require('config/registerFaculty'); 
var registerAnswer = require('config/registerAnswer'); 
var mime = require('mime');
var fs = require('fs');
 var util = require('util');
 var user=require('config/models');
 
module.exports = function(app) {


app.get('/download', function(req, res){

var filename=req.param('name');
var path=require('path');
 var dirname = path.resolve(".");

  var file = dirname+'/Files/'+filename;
	console.log(file);

  var filename = path.basename(file);
  var mimetype = mime.lookup(file);
//res.setHeader('Content-disposition', 'attachment; filename=' + filename);
  //res.setHeader('Content-type', mimetype);

  var filestream = fs.createReadStream(file,{ bufferSize: 64 * 1024 });
  filestream.pipe(res);
});


app.get('/video',function (req, res) {
  var filename=req.param('name');
var path=require('path');
 var dirname = path.resolve(".");
  var f = dirname+'/Videos/'+filename;
	console.log(f);
  var stat = fs.statSync(f);
  var total = stat.size;
  if (req.headers['range']) {
    var range = req.headers.range;
    var parts = range.replace(/bytes=/, "").split("-");
    var partialstart = parts[0];
    var partialend = parts[1];

    var start = parseInt(partialstart, 10);
    var end = partialend ? parseInt(partialend, 10) : total-1;
    var chunksize = (end-start)+1;
    console.log('RANGE: ' + start + ' - ' + end + ' = ' + chunksize);

    var file = fs.createReadStream(f, {start: start, end: end});
    res.writeHead(206, { 'Content-Range': 'bytes ' + start + '-' + end + '/' + total, 'Accept-Ranges': 'bytes', 'Content-Length': chunksize, 'Content-Type': 'video/mp4' });
    file.pipe(res);
  } else {
    console.log('ALL: ' + total);
    res.writeHead(200, { 'Content-Length': total, 'Content-Type': 'video/mp4' });
    fs.createReadStream(f).pipe(res);
  }
});

app.post('/videoList',function(req,res){
var path=require('path');
 var dirname = path.resolve(".");
fs.readdir(dirname+"/Videos", function(err, files) {
    if (err) return;
var i=0;
var fileMap=[];
    files.forEach(function(f) {
		
		fileMap.push({"File":f});
		i++;
        });
  console.log(fileMap);
  res.json({'Files':fileMap});
});
});



app.post('/filesList',function(req,res){
var path=require('path');
 var dirname = path.resolve(".");
fs.readdir(dirname+"/Files", function(err, files) {
    if (err) return;
var i=0;
var fileMap=[];
    files.forEach(function(f) {
		
		fileMap.push({"File":f});
		i++;
        });
  console.log(fileMap);
  res.json({'Files':fileMap});
});
});

app.post('/registerForum',function(req,res){
var quesUser= req.body.quesUser;             
               var ques= req.body.ques;       
   registerForum.register(quesUser,ques,function (found) {           
               console.log(found);             
               res.json(found);    
     });    

});

app.post('/registerAnswer',function(req,res){
var quesUser = req.body.quesUser;             
               var ans = req.body.ans;
var ques= req.body.ques;
       		var ansUser = req.body.ansUser;
 registerAnswer.register(quesUser,ques,ans,ansUser,function (found) {           
               console.log(found);             
               res.json(found);    
     });    

});


app.post('/forumList', function(req, res) {
  user.forum.find({}, function(err, users) {
    var userMap = {};
var i=0;
    users.forEach(function(user) {

      userMap[i] = user;
i++;
	
    });
	console.log(userMap);
    res.json(userMap);  
  });
});

app.post('/upload', function(req, res) {
var path=require('path');
 var dirname = path.resolve("."); 
fs.rename(req.files.image.path,dirname+"/Files/"+req.files.image.name, function (err) {
  if(err != null)
         res.json({'responce':"Error in uploading"});

});
        fs.readFile(req.files.image.path, function (err, data){

        var newPath = dirname+"/Files/" + req.files.image.originalFilename;
        fs.writeFile(newPath, data, function (err) {
        if(err){
        res.json({'response':"Error in uploading"});
        }else {
console.log(req.files.image.originalFilename);
    console.log(req.files.image.path);
        res.json({'response':"Saved"});
}
});
});
});
 
 
app.get('/uploads/:file', function (req, res){
         var path=require('path');

    file = req.params.file;

    var dirname = path.resolve(".");

    var img = fs.readFileSync(dirname  + file);
        res.writeHead(200, {'Content-Type': 'image/jpg' });
        res.end(img, 'binary');
 
}); 



     app.get('/', function(req, res) {       

          res.end("Node-Android-Project");    
     });     

     app.post('/login',function(req,res){        
          var email = req.body.email;             
               var password = req.body.password;       

          login.login(email,password,function (found) {           
               console.log(found);             
               res.json(found);    
     });    
     });     

     app.post('/loginFaculty',function(req,res){        
          var email = req.body.email;             
               var password = req.body.password;       

          loginFaculty.login(email,password,function (found) {           
               console.log(found);             
               res.json(found);    
     });    
     });     

     app.post('/register',function(req,res){         
          var email = req.body.email;             
               var password = req.body.password;       
		var name=req.body.name;
		var branch=req.body.branch;
          register.register(email,password,name,branch,function (found) {             
               console.log(found);             
               res.json(found);    
     });     
     });     

app.post('/registerFaculty',function(req,res){         
          var email = req.body.email;             
               var password = req.body.password;       
		var name=req.body.name;
		var branch=req.body.branch;
          registerFaculty.register(email,password,name,branch,function (found) {             
               console.log(found);             
               res.json(found);    
     });     
     });   
     app.post('/api/chgpass', function(req, res) {       
          var id = req.body.id;                 
               var opass = req.body.oldpass;         
          var npass = req.body.newpass;       

          chgpass.cpass(id,opass,npass,function(found){           
               console.log(found);             
               res.json(found);    
     });     
     });     

     app.post('/api/resetpass', function(req, res) {         

          var email = req.body.email;         

          chgpass.respass_init(email,function(found){             
               console.log(found);             
               res.json(found);    
     });     
     });     

     app.post('/api/resetpass/chg', function(req, res) {         
          var email = req.body.email;         
          var code = req.body.code;       
          var npass = req.body.newpass;       

     chgpass.respass_chg(email,code,npass,function(found){           
          console.log(found);             
          res.json(found);    
     
     });     
     });  
};