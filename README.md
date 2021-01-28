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

1. Download Project. In Node JS server , Do ***npm install***. it will install mongoDb and nodejs in system.

          - I have not uploaded all node modules, only config folder it there. taking config folder as reference, we can create more modules
          - once the node modules get installed, copy the ![config](https://github.com/vimaltiwari2612/VirtualClassroom/tree/master/node%20JS%20Server/node_modules/config) config folder and paste it inside node_module's folder created due t npm install.
    
2. install the apk in your phone

          - if you want to customize the apk, open it in android studio
          - change the folder name from "Android Client" to "Virtual Classroom", because the package structure expects "virtualclassroom" as name.
          - Have use my own PC ip4 address with port 8080, so change the IPv4 address to yours one. run "ipconfig" on windows cmd, for linux, i don't know the commnand :P
          
3. MAKE SURE TO CONNECT YOUR PHONE AND SERVER WITH A COMMON NETWORK OR HOTSPOT
4. run below commands in cmd:
    
@echo Off
cd\
cd C:\Program Files\MongoDB\Server\3.2\bin
mongod.exe --dbpath "Your Path upto Database folder of this app"


5. open another cmd  and run :

@echo Off
cd\
cd "Your Path upto server folder of this app" 
node app.js

          - If you get error like throw err, "MODULE NOT FOUND", while running the node server, do below activites.
                    - go to route.js and check the require modules. 
                    - for each require module, you should be having a file with same name inside node_module/config
                    - if some file is not present, just comment it's "require" statement in route.js

6. open this android app and use it. HAVE FUN!!!

# Screenshots

![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-23-18.png)
![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-23-41.png)
![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-24-15.png)
![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-24-09.png)
![Screenshot](https://github.com/vimaltiwari2612/VirtualClassroom/blob/master/Screenshots/Screenshot_2016-05-14-09-25-04.png)

FOR MORE REFERENCE , REFER TO SCREENSHOT FOLDER!!

# Inspiration
https://www.learn2crack.com/2014/04/android-login-registration-nodejs-server.html 

