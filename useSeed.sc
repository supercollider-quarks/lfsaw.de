+ Thread {
	useSeed {|seed, function|
		var val;
		var randData = this.randData;
		this.randSeed = seed;
		val = function.value;
		this.randData = randData;
		^val;
	}
}
