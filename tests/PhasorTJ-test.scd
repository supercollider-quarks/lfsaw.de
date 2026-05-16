
Ndef(\a, {
	PhasorTJ.ar(\freq.kr(1), \jitter.kr(1), \jFreq.kr(1), \jType.kr(0), \pType.kr(0))
})

.edit

(
Ndef(\a).addSpec(
	\freq, [0, 200],
	\jitter, [0, 20], // relative to cycles
	\jFreq, [0, 50],
	\jType, [0, 2, \lin, 1, 1],
	\pType, [0, 2, \lin, 1, 1]
);
Ndef(\a, {|freq|

	[SinOsc.ar(0,
		PhasorTJ.ar(freq, 2pi, \jitter.kr(1), \jFreq.kr(1), \jType.kr(0), \pType.kr(0))
	),
	SinOsc.ar(freq)]
})
)


Ndef(\a).edit

RotateL.ar(1, [SinOsc.ar])

LFTri().signalRange

LFSaw().signalRange


SynthDef(\a, {PhasorTJ.ar(\freq.kr(1), \jitter.kr(1), \jFreq.kr(1), \cub).linlin(0, 1, 0, 2pi)}).dumpUGens


q = ();

q.buf = Buffer.read(s, "/localvol/sound/supercollider/oneShots/new/bounceSaw/aiff24/*.aiff".pathMatch.first);

q.buf.play

x = {
	var buf = q.buf;
	BufRd.ar(
		numChannels: q.buf.numChannels,
		bufnum: buf,
		phase: PhasorTJ.ar(BufDur.ir(buf).reciprocal) * BufFrames.ir(buf),
		loop: 1,
		interpolation: 2
	);
}.play;

x.free

SCDoc.indexAllDocuments(true)