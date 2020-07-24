var crypto = require('crypto');
var rand = require('csprng');
var mongoose = require('mongoose');
var nodemailer = require('nodemailer');
var user = require('config/models');

var smtpTransport = nodemailer.createTransport("SMTP",{
    auth: {
        user: "user@gmail.com", 
        pass: "*********"
    	}
});


exports.cpass = function(id,opass,npass,callback) {

var temp1 =rand(160, 36);
var newpass1 = temp1 + npass;
var hashed_passwordn = crypto.createHash('sha512').update(newpass1).digest("hex");

user.find({token: id},function(err,users){

if(users.length != 0){

var temp = users[0].salt;
var hash_db = users[0].hashed_password;
var newpass = temp + opass;
var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex");


if(hash_db == hashed_password){
if (npass.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/) && npass.length > 4 && npass.match(/[0-9]/) && npass.match(/.[!,@,#,$,%,^,&,*,?,_,~]/)) {

user.findOne({ token: id }, function (err, doc){
  doc.hashed_password = hashed_passwordn;
  doc.salt = temp1;
  doc.save();

callback({'response':"Password Sucessfully Changed",'res':true});

});
}else{ 

callback({'response':"New Password is Weak. Try a Strong Password !",'res':false});

}
}else{

callback({'response':"Passwords do not match. Try Again !",'res':false});

}
}else{

callback({'response':"Error while changing password",'res':false});

}

});
}

exports.respass_init = function(email,callback) {

var temp =rand(24, 24);
user.find({email: email},function(err,users){

if(users.length != 0){


user.findOne({ email: email }, function (err, doc){
  doc.temp_str= temp;
  doc.save();

var mailOptions = {
    from: "Raj Amal  <raj.amalw@gmail.com>", 
    to: email,
    subject: "Reset Password ", 
    text: "Hello "+email+".  Code to reset your Password is "+temp+".\n\nRegards,\nRaj Amal,\nLearn2Crack Team.",
    
}

smtpTransport.sendMail(mailOptions, function(error, response){
    if(error){

callback({'response':"Error While Resetting password. Try Again !",'res':false});

    }else{

callback({'response':"Check your Email and enter the verification code to reset your Password.",'res':true});

    }
});
});
}else{

callback({'response':"Email Does not Exists.",'res':false});

}
});
}

exports.respass_chg = function(email,code,npass,callback) {


user.find({email: email},function(err,users){

if(users.length != 0){

var temp = users[0].temp_str;
var temp1 =rand(160, 36);
var newpass1 = temp1 + npass;
var hashed_password = crypto.createHash('sha512').update(newpass1).digest("hex");

if(temp == code){
if (npass.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/) && npass.length > 4 && npass.match(/[0-9]/) && npass.match(/.[!,@,#,$,%,^,&,*,?,_,~]/)) {
user.findOne({ email: email }, function (err, doc){
  doc.hashed_password= hashed_password;
  doc.salt = temp1;
  doc.temp_str = "";
  doc.save();

callback({'response':"Password Sucessfully Changed",'res':true});

});}else{

callback({'response':"New Password is Weak. Try a Strong Password !",'res':false});

}
}else{

callback({'response':"Code does not match. Try Again !",'res':false});

}
}else{

callback({'response':"Error",'res':true});

}
});
}

