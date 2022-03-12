MyBalance2 : MultiOutUGen {

	*ar { arg in, pos = 0.0, level = 1.0;
		^Balance2.ar(in[0], in[1], pos, level)
	}
	*kr { arg in, pos = 0.0, level = 1.0;
		^Balance2.kr(in[0], in[1], pos, level)
	}
}


MSPan2 : MultiOutUGen {
	*ar { arg in, pos, width, level;
		var m, s, l, r;
		m = in[0] + in [1];
		s = in[0] - in [1];
		s = (s * width);

		l =	m + s;
		r =	m - s;

		^Balance2.ar(l, r, pos, level);
	}
}
