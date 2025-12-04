IdxItemsSpec {
	var <items, <warp, <default, <spec, size;

	*new { |items, warp = 0, default|
		^super.newCopyArgs( items, warp, default ).init;
	}

	init {
		size = items.size;
		spec = [ -0.5, size - 0.5, warp, 1].asSpec;
		// default is in mapped range, not unmapped 0
		// if no default given, take first key
		if (items.includes(default).not) { default = items[0] };
	}

	// from number to item - create equal parts of the range
	map { |inval|
		^items.clipAt( spec.map( inval/size ).asInteger );
	}

	// from string to number
	unmap { |inval|
		var index = items.indexOfEqual( inval );
		if ( index.notNil ){
			^(spec.unmap( index ) * size).asInteger;
		};
		^nil;
	}

	asSpec {
		^this;
	}

	warp_ { |argWarp| spec.warp_(argWarp) }

}

ReverseIdxItemsSpec {
	var <items, <warp, <default, <spec, size;

	*new { |items, warp = 0, default|
		^super.newCopyArgs( items, warp, default ).init;
	}

	init {
		size = items.size;
		spec = [ -0.5, size - 0.5, warp, 1].asSpec;
		// default is in mapped range, not unmapped 0
		// if no default given, take first key
		if (items.includes(default).not) { default = items[0] };
	}

	// from number to item - create equal parts of the range
	unmap { |inval|
		^items.clipAt( spec.map( inval/size ).asInteger );
	}

	// from string to number
	map { |inval|
		var index = items.indexOfEqual( inval );
		if ( index.notNil ){
			^(spec.unmap( index ) * size).asInteger;
		};
		^nil;
	}

	asSpec {
		^this;
	}

	warp_ { |argWarp| spec.warp_(argWarp) }

}

