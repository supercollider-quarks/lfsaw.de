+ Ndef {
	stateCS {
		^this.nodeMap.asCode(this.asString, true);
	}
	pbcopyState {
		this.stateCS.pbcopy
	}
}

+ String {
	pbcopy {
		// copy to OSX clipboard
		"echo '%' | pbcopy".format(this).systemCmd
	}
}
