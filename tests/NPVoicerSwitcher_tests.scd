// Assumes NPVoicerSwitcher is implemented and classlib recompiled.

q = q ? (); // ensure q exists for dict access

q.assert = {|q, condition, failureMsg|
    if (condition.value.not) {
        Error(failureMsg).throw;
    };
    true
};

// 1) Build switcher and assign explicit roles:
q.sw = NPVoicerSwitcher(3, NPVoicer); // current + saved(0..2) + released(0..2)

q.sw.allVoicers.do{|voicer|
    voicer.proxy.postln;
};

q.sw.prime(\default); // assign default roles to all slots

q.sw.current.inspect

s.boot

q.sw.allVoicers.do{|voicer|
    voicer.proxy.postln;
}

// get all voicers to later in the process check if they were correctly moved around
q.voicers = q.sw.allVoicers.copy; // copy to avoid mutating the original array in the switcher

q.assert({q.voicers[0] === q.sw.current    }, "current should be voicers[0]");
q.assert({q.voicers[1] === q.sw.saved[0]   }, "saved(0) should be voicers[1]");
q.assert({q.voicers[2] === q.sw.saved[1]   }, "saved(1) should be voicers[2]");
q.assert({q.voicers[3] === q.sw.saved[2]   }, "saved(2) should be voicers[3]");
q.assert({q.voicers[4] === q.sw.released[0]}, "released(0) should be voicers[4]");
q.assert({q.voicers[5] === q.sw.released[1]}, "released(1) should be voicers[5]");
q.assert({q.voicers[6] === q.sw.released[2]}, "released(2) should be voicers[6]");



q.sw.swapCurrentToSaved(2); // swap: current->saved(2), old saved(2)->released(2), old released(2)->current

q.assert({q.voicers[6] === q.sw.current    }, "current should be old released(2) = voicers[6]");
q.assert({q.voicers[1] === q.sw.saved[0]   }, "saved(0) should be unchanged = voicers[1]");
q.assert({q.voicers[2] === q.sw.saved[1]   }, "saved(1) should be unchanged = voicers[2]");
q.assert({q.voicers[0] === q.sw.saved[2]   }, "saved(2) should be old current = voicers[0]");
q.assert({q.voicers[4] === q.sw.released[0]}, "released(0) should be unchanged = voicers[4]");
q.assert({q.voicers[5] === q.sw.released[1]}, "released(1) should be unchanged = voicers[5]");
q.assert({q.voicers[3] === q.sw.released[2]}, "released(2) should be old saved(2) = voicers[3]");
