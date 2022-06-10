const { exit } = require('process');
const yargs = require('yargs');
const fs = require('fs');

const argv = yargs
  .option('env', {
    alias: 'e',
    description: 'Set the build environment to "production" or "development"',
    type: 'string',
  })
  .help()
  .alias('help', 'h').argv;

if (!argv.env) {
  console.error('env option is missing');
  exit(-1);
}

if (fs.existsSync('.env.' + argv.env + '.local')) {
  fs.copyFileSync('.env.' + argv.env + '.local', '.env');
  console.log('.env.' + argv.env + '.local copied');
} else if (fs.existsSync('.env.' + argv.env)) {
  fs.copyFileSync('.env.' + argv.env, '.env');
  console.log('.env.' + argv.env + ' copied');
} else {
  console.error('File .env.' + argv.env + ' or .env.' + argv.env + '.local does not exist!');
  exit(-1);
}

const data = fs.readFileSync('.env');
const fd = fs.openSync('.env', 'w+');
const insert = Buffer.from('# This file is auto-generated by ./script/prebuild.ts. \n# Do not change it manually!\n');
fs.writeSync(fd, insert, 0, insert.length, 0);
fs.writeSync(fd, data, 0, data.length, insert.length);
fs.close(fd, (err) => {
  if (err) throw err;
});

exit(0);