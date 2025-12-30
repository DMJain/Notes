const Swimmable = require('../contract/Swimmable');

/**
 * Fish class implementing Swimmable
 */
class Fish extends Swimmable {
    swim() {
        console.log("Fish is swimming in the water.");
    }
}

module.exports = Fish;
