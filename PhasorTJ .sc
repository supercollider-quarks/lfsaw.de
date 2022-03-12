PhasorTJ {
	classvar jitterEngines, phaseEngines;

	*initClass {
		jitterEngines = IdentityDictionary[
			\step   -> {|freq, selector = \ar| LFDNoise0.perform(selector, freq)},
			\hold   -> {|freq, selector = \ar| LFDNoise0.perform(selector, freq)},
			\lin    -> {|freq, selector = \ar| LFDNoise1.perform(selector, freq)},
			\linear -> {|freq, selector = \ar| LFDNoise1.perform(selector, freq)},
			\cub    -> {|freq, selector = \ar| LFDNoise3.perform(selector, freq)},
			\cubed  -> {|freq, selector = \ar| LFDNoise3.perform(selector, freq)},
			\tri   -> {|freq, selector = \ar|  LFTri    .perform(selector, freq)},
			\sine   -> {|freq, selector = \ar| SinOsc   .perform(selector, freq)},
			\sin    -> {|freq, selector = \ar| SinOsc   .perform(selector, freq)}
		];
		phaseEngines = IdentityDictionary[
			\saw -> {|freq, end, offset, selector = \ar| LFSaw.perform(selector, freq, (1 + offset)%2).range(0, end)},
			\tri -> {|freq, end, offset, selector = \ar| LFTri.perform(selector, freq * 0.5, (offset * 2)%4).range(0, end)},
			\sin -> {|freq, end, offset, selector = \ar| SinOsc.perform(selector, freq * 0.5, (1.5pi + (offset * pi))%2pi).range(0, end)},
		];
	}

	*jitterEngine {|name, freq, selector|
		var engine;

		// UGen function defined externally
		// name.isKindOf(AbstractFunction).if{
		// 	^name.value(freq, selector)
		// };
		name.isKindOf(Symbol).if{
			engine = jitterEngines.at(name);
			if(engine.isNil) {
				Error("%: jitterType % not defined.").format(this, name).throw
			};
			^engine.value(freq, selector);
		};
		// default case
		engine = Select.perform(selector, name, [
			LFDNoise0.perform(selector, freq),
			LFDNoise1.perform(selector, freq),
			LFDNoise3.perform(selector, freq),
			LFTri.perform(selector, freq),
			SinOsc.perform(selector, freq),
		]);
		^engine
	}

	*phaseEngine {|name, freq, end, offset, selector|
		var engine;

		// UGen function defined externally
		// name.isKindOf(AbstractFunction).if{
		// 	^name.value(freq, selector)
		// };
		name.isKindOf(Symbol).if{
			engine = phaseEngines.at(name);
			if(engine.isNil) {
				Error("%: phaseType % not defined.").format(this, name).throw
			};
			^engine.value(freq, end, offset, selector);
		};
		engine = Select.perform(selector, name, [
			LFSaw .perform(selector, freq, (1 + offset)%2).range(0, end),
			LFTri .perform(selector, freq * 0.5, (offset * 2)%4).range(0, end),
			SinOsc.perform(selector, freq * 0.5, (1.5pi + (offset * pi))%2pi).range(0, end)
		]);
		^engine
	}

	// pseudo-analog tape jitter
	*new1 {|rate, freq = 1, end = 1, offset = 0, jitter = 0, jitterFreq = 0.1, jitterType = \lin, phaseType = \saw|
		var selector = UGen.methodSelectorForRate(rate);
		var phaseJitter, phase, phaseEngine, jitterEngine;

		//[\freq, freq, \jitter, jitter, \jFreq, jitterFreq, \jType, jitterType, \pType, phaseType].postln;
		phaseJitter = this.jitterEngine(jitterType, jitterFreq, selector) * jitter;
		phase  = this.phaseEngine ( phaseType, freq * (1+phaseJitter), end, offset, selector);
		^phase
	}
	*ar {|freq = 1, end = 1, offset = 0, jitter = 0, jitterFreq = 0.1, jitterType = \lin, phaseType = \saw|
		^this.new1(\audio, freq, end, offset, jitter, jitterFreq, jitterType, phaseType);
	}
	*kr {|freq = 1, end = 1, offset = 0, jitter = 0, jitterFreq = 0.1, jitterType = \lin, phaseType = \saw|
		^this.new1(\control, freq, end, offset, jitter, jitterFreq, jitterType, phaseType);
	}

}