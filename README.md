# Spread-the-light
Code vs Covid 2019 Hackathon project

## Requirements
This project is built with Ionic.
Make sure to install the following software on your machine:
+ [Ionic](https://ionicframework.com/)
+ [NodeJS](http://nodejs.org/)

## Installation

### Intall NodeJS
```bash
$ curl -sL https://deb.nodesource.com/setup_13.x | sudo -E bash -
$ sudo apt-get install -y nodejs
```

### Install Ionic
```bash
$ npm install -g @ionic/cli
```

### Install modules
Navigate to the repository and execute the following command to install all necessary node modules:
```bash
$ npm install
```

## Run
To start the application, run the following command in the main folder:
```bash
$ ionic serve
```
When succesful, the application will run on http://localhost:8100/


## Android native
1. Install android studio
2. run `ionic build` (run once)
2. run `ionic capacitor add android` (run once)
3. run `ionic capacitor copy android` (run this command to build the whole android thing)
4. run `npx cap open android` (opens android studio with the correct config, run once)
5. Connect your android phone, enable usb debugging in developers mode. Your phone should show up in android studio right top corner
6. Run android the project by clicking the green play button in android studio.

## [IOS native](https://ionicframework.com/docs/building/ios)
1. Install XCode
2. run `ionic capacitor add ios`
3. run `ionic capacitor open ios`. This will open XCode. In XCode...
4. under `General` in the top bar and `Identity`, check that the `Bundle Identifier` is the same as the `AppId` in `capacitor.config.json`.
5. under `General` and `Signing`, ensure Automatically manage signing is enabled.
6. run `ionic capacitor copy ios`
7. In XCode, select a target device (Simulator or connected iPhone) and hit the play button.