var crypto = require('crypto');
var rand = require('csprng');
var mongoose = require('mongoose');
var gravatar = require('gravatar');
var user = require('config/models');

exports.login = function(email,password,callback) {

user.find({email: email},function(err,users){

if(users.length != 0){

var temp = users[0].salt;
var hash_db = users[0].hashed_password;
var id = users[0].token;
var newpass = temp + password;
var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex");
var grav_url = gravatar.url(email, {s: '200', r: 'pg', d: '404'});
if(hash_db == hashed_password){

callback({'response':"Login Sucess",'res':true,'token':id,'grav':grav_url});

}else{

callback({'response':"Invalid Password",'res':false});

}
}else {

callback({'response':"User not exist",'res':false});

}
});
}
