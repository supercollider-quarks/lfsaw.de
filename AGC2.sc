AGC2 {
	// a simple limiter
	*ar{|in, maxLookahead = 0.01, lookahead = 0.01, limit|

		var rms500 = RMS.ar(in, 500);
		var rms1   = RMS.ar(in, 10) * 2;
		var analytics = max(0, rms500 - rms1) + rms1;

		^(
			DelayC.ar(in, maxLookahead, lookahead)
			/ max(0.125, analytics / limit)
		).clip2(1);
	}
	*kr{|in, maxLookahead = 0.01, lookahead = 0.01, limit|

		var rms500 = RMS.kr(in, 500);
		var rms1   = RMS.kr(in, 10) * 2;
		var analytics = max(0, rms500 - rms1) + rms1;

		^(
			DelayC.kr(in, maxLookahead, lookahead)
			/ max(0.125, analytics / limit)
		).clip2(1);
	}
}
