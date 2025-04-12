# BlogAngularUi

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 9.1.0.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).



## How to update the local angular-cli version

    npm install --save-dev @angular/cli@latest
    npm i 
    ng update @angular/cli 
    ng update @angular/core

    if repository is not saved then use an `allow-dirty` flag to bypass the repo check.
    e.g ng update @angular/cli @angular/core --allow-dirty
	
use npm outdated to check the available updates
	
	
## Update using npm-update-ckeck package 
 
This is a package available at npmjs.org , named npm-update-check, which works the same as npm update command. 
 
But the difference is that it's a utility that automatically adjusts packages which are listed into package.json file , whenever any updates are required. For that we need to install it via command: 
> npm install -g npm-check-updates  

After installing the above package to a global location, now we will be able to update packages automatically. The next step is to update all the packages by running the command:
> ncu -u // tells to update via npm package  

And then install it via npm install.
