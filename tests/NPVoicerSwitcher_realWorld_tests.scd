// Assumes NPVoicerSwitcher is implemented and classlib recompiled.


(
q = q ? (); // ensure q exists for dict access
s.waitForBoot {
    SynthDef(\sine, { |out = 0, freq = 440, gate = 1|
        var sig = SinOsc.ar(freq) * 0.1;
        var env = Env.asr(0.01, 1, 0.1).kr(doneAction: 2, gate: gate);
        sig = sig * env;
        Out.ar(out, sig!2);
    }).add;
    ProxyMeter.addMixer(NdefMixer(s))
};
)

// 1) Build switcher and assign explicit roles:
q.sw = NPVoicerSwitcher(3, NPVoicer); // current + saved(0..2) + released(0..2)

q.sw.prime(\sine); // assign synth
q.sw.play


q.sw.current.put(48, [\freq, 48.midicps]);
    // a second note at 51
q.sw.current.put(51, [\freq, 51.midicps]);
q.sw.current.release(51, 10);

q.sw.swapCurrentToSaved(2, fadeTime: 2); // swap: current->saved(2), old saved(2)->released(2), old released(2)->current

q.sw.current.put(55, [\freq, 55.midicps]);
