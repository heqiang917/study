// https://github.com/shelljs/shelljs
require('./check-versions')()
require('shelljs/global')
var optimist =require('optimist');
env.NODE_ENV = optimist.argv.env||'dev';
var path = require('path')
var config = require('../config')
var ora = require('ora')
var webpack = require('webpack')
var webpackConfig = require('./webpack.prod.conf')


var spinner = ora(`building for ${env.NODE_ENV}...`)
spinner.start()

var assetsPath = path.join(config.build.assetsRoot, config.build.assetsSubDirectory);
var templatePath = path.join(config.build.assetsRoot, '/template');
rm('-rf', assetsPath)
rm('-rf', templatePath)
mkdir('-p', assetsPath)
cp('-R', 'static/*', assetsPath)

webpack(webpackConfig, function (err, stats) {
  spinner.stop()
  if (err) throw err
  process.stdout.write(stats.toString({
    colors: true,
    modules: false,
    children: false,
    chunks: false,
    chunkModules: false
  }) + '\n')
})
