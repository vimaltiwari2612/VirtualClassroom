# VirtualClassroom

***An app for making a single platform available to students and faculties to interact with each other online.***
Features –
- Live Streaming of Lecture videos available at server.
- Forum for Discussions .
- File uploading and downloading.

# prerequisite
1. MongoDB
2. nodejs
3. IDE for android development

# components
1. android src (Client side code)
2. node js server contains(imp):

          - Files : drop files here which can be accessed by Clients
          - Video : drop videos here which can be streamed by clients
          - app.js (to run the server)
          - routes.js : wiring between client and server
3. Database : for saving the logs (system generated)


# howToRunIt

1. install mongoDb and nodejs in system.
2. install the apk in your phone
3. MAKE SURE TO CONNECT YOUR PHONE AND SERVER WITH A COMMON NETWORK OR HOTSPOT
4. run below commands in cmd:
    
@echo Off
cd\
cd C:\Program Files\MongoDB\Server\3.2\bin
mongod.exe --dbpath "Your Path upto Database folder of this app"


4. open another cmd  and run :

@echo Off
cd\
cd "Your Path upto server folder of this app"
node app.js

5. open this android app and use it. HAVE FUN!!!

![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-23-18.png)
![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-23-41.png)
![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-24-15.png)
![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-24-09.png)
![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-25-04.png)

FOR MORE REFERENCE , REFER TO SCREENSHOT FOLDER!!

# Inspiration
https://www.learn2crack.com/2014/04/android-login-registration-nodejs-server.html 

