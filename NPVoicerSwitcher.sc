/* 
Changelog:
+ 2026-04-26 — initial implementation


ToDo:
+ add prefix argument for Ndef naming to constructor
*/

NPVoicerSwitcher {
    var <numSaves;
    var <voicerClass;

    var <current;
    var <saved;
    var <released;
    var <prefix;

    var <>swapAction;

    *new { |numSaves = 3, voicerClass (NPVoicer), prefix|
        ^super.new.init(numSaves, voicerClass, prefix)
    }

    init { |argNumSaves, argVoicerClass, argPrefix|
        numSaves = argNumSaves;
        voicerClass = argVoicerClass;
        prefix = argPrefix ? "";

        current = this.pr_makeVoicer(0);
        saved = numSaves.collect { |i|
            this.pr_makeVoicer(i + 1)
         };
        released = numSaves.collect { |i|
            this.pr_makeVoicer(i + 1 + numSaves)
         };
    }

    // prime sounds for all voicers or for a specific voicer role (current, saved, released)
    prime { |obj, useSpawn = false, which|
        which.isNil.if({
            this.allVoicers.do {|voicer|
                voicer.prime(obj, useSpawn);
            };
        }, {
            case
            { which == \current } { current.prime(obj, useSpawn) }
            { which == \saved } {
                saved.do {|voicer| voicer.prime(obj, useSpawn) }
            }
            { which == \released } {
                released.do {|voicer| voicer.prime(obj, useSpawn) }
             }
        });
    }

    allVoicers {
         ^[current] ++ saved ++ released
    }

    swapCurrentToSaved { |idx, fadeTime = 5|
        var oldCurrent = current;
        var oldSaved = saved[idx];
        var oldReleased = released[idx];

        if(oldSaved.notNil and: { oldSaved.respondsTo(\releaseAll) }) {
            oldSaved.releaseAll(fadeTime);
        };

        released[idx] = oldSaved;
        saved[idx] = oldCurrent;
        current = oldReleased;

        swapAction.value(this, idx);
        ^this
    }


    ///// minimal NPVoicer interface

    // play is forwarded to all nodeproxies of all voicers
    // arg outs is either one number (same for all voicers) or an array of numbers (one per voicer)
    play {| outs, numChannels, group, multi=false, vol, fadeTime, addAction |
        outs = outs.asArray; // ensure outs is an array (works even if nil)
        this.allVoicers.do {|voicer, i|
            voicer.play(outs.wrapAt(i), numChannels, group, multi, vol, fadeTime, addAction);
        };
    }

    stop { |fadeTime = 5|
        this.allVoicers.do {|voicer|
            voicer.stop(fadeTime);
        };
    }

    releaseAll { |fadeTime = 5|
        this.allVoicers.do {|voicer|
            voicer.releaseAll(fadeTime);
        };
    }

    //// private
    pr_makeVoicer { |idx|
        ^voicerClass.new(Ndef("%%".format(prefix, idx).asSymbol));
    }


}




/*
// create a switcher for 1 current + 3 saved voicers
a = NPVoicerSwitcher(numSaves: 3, class: NPVoicer); // allow to save up to 3 voicers, using the NPVoicer class for all voicer objects

// this requires 7 voicer objects in the patch:
a.allVoicers;
// -> [voicerA, voicerB, voicerC, voicerD, voicerE, voicerF, voicerG]

a.current
// -> current voicer object

a.saved(n)
// -> saved voicer object for slot n (1-3)

a.released(n)
// -> released voicer object for slot n (1-3)


a.saved
// -> array of all saved voicer objects (here: length 3)

a.released
// -> array of all released voicer objects (here: length 3)


Scenario 1: swap current voicer to saved(2) (indexing starts at 0) with release slot

a.swapCurrentToSaved(2)


state before
    - a.current: voicerA (active)
    - a.saved: [ voicerB, voicerC, voicerD ]
    - a.released: [ voicerE, voicerF, voicerG ]


1. `a.saved(2).releaseAll` releases all voices in the voicer currently in saved(2).
    state:
2. move that just-released voicer to released(2).
3. move the current voicer to saved(2).
4. move the previously released(2) voicer to current.
5. execute a first-level function:
    - `a.swapAction(a)` is a function that can be defined by the user to specify what should happen after the swap. It receives the switcher object as an argument, so it can access the current, saved, and released voicers to perform any necessary actions (e.g., triggering notes, changing parameters, etc.).

state after
    - a.current: voicerG (new active)
    - a.saved: [ voicerB, voicerC, voicerA ]
    - a.released: [ voicerE, voicerF, voicerD ]




Meaning:
- voicerD is the one being faded out, but it is still kept as the release voicer.
- voicerA leaves the current role and becomes the saved voicer for slot 2.
- voicerG becomes the new active voicer.

*/
