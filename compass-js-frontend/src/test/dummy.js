var assert = require("chai").assert;
var expect = require("chai").expect;
var should = require("chai").should(); // actually call the function
var dummy = "Dummy";

describe("Dummy", function () {
	describe("Dummy", function () {
		it("should never fail, since this is DUMMY test", function () {
			assert.typeOf(dummy, "string");
			expect(dummy).to.be.a("string");
			dummy.should.be.a("string");
		});
	});
	describe("Dummy", function () {
		it("should always fail, since this is DUMMY test", function () {
			assert.typeOf(dummy, "number");
			expect(dummy).to.be.a("number");
			dummy.should.be.a("number");
		});
	});
});
