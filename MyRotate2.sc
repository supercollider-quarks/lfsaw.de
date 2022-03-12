MyRotate2 : MultiOutUGen {
	
	*ar { arg in, pos = 0.0;
		^Rotate2.ar(in[0], in[1], pos)
	}
	*kr { arg in, pos = 0.0;
		^Rotate2.kr(in[0], in[1], pos)
	}
}