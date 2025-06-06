// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html

module.exports = function (config) {
  // Check for Chrome binary path environment variable
  process.env.CHROME_BIN = process.env.CHROME_BIN || require('puppeteer').executablePath();
  
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage'),
      require('@angular-devkit/build-angular/plugins/karma')
    ],
    // Force Karma to exit with an error if there are test failures
    failOnFailingTestSuite: true,
    client: {
      clearContext: false // leave Jasmine Spec Runner output visible in browser
    },
    coverageIstanbulReporter: {
      dir: require('path').join(__dirname, './coverage/blog-angular-ui'),
      reports: ['html', 'lcovonly', 'text-summary'],
      fixWebpackSourcePaths: true
    },
    reporters: ['progress', 'kjhtml'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    browsers: ['Chrome'],
    singleRun: false,
    restartOnFileChange: true,
    customLaunchers: {
      ChromeHeadlessCustom: {
        base: 'ChromeHeadless',
        flags: ['--no-sandbox', '--disable-gpu']
      }
    },
    browserNoActivityTimeout: 120000,
    browserDisconnectTimeout: 10000,
    browserDisconnectTolerance: 3,
    failOnEmptyTestSuite: true,
    failOnSkippedTests: false,
    detailedReporter: {
      showSkipped: true,
      showFailed: true,
      maxFailedTestsToShow: 100
    }
  });
};
