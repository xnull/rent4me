//var Person = require("babel!./Person.js").default;

import Person from './Person.js';

var p = new Person("Dionisij");

module.exports = "It works from content.js! "+p;
